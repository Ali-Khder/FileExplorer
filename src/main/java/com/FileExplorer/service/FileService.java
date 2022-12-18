package com.FileExplorer.service;

import com.FileExplorer.entity.File;
import com.FileExplorer.entity.Folder;
import com.FileExplorer.entity.User;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.interfaces.selectedOnlyForReports;
import com.FileExplorer.repository.FileRepository;
import com.FileExplorer.repository.FolderRepository;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import com.FileExplorer.service.properties.FilesProperties;
import com.FileExplorer.utils.ConfigUtility;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.*;

@Service
public class FileService {
    private int maxUpload = 0;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ConfigUtility configUtility;
    private final FolderRepository folderRepository;

    public FileService(UserRepository userRepository,
                       FileRepository fileRepository,
                       ConfigUtility configUtility,
                       FolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.configUtility = configUtility;
        this.folderRepository = folderRepository;
    }

//    @Bean
//    int getMaxUpload() {
//        return filesProperties.maxUpload();
//    }

    @Cacheable(cacheNames = "files")
    public List<File> getAll(Long id) {
        System.out.println("DB get all");
        return folderRepository.findById(id).get().getFiles();
    }

    public List<selectedOnlyForReports> getReports() {
        return fileRepository.findAllByOrderByCreatedAtDesc();
    }

    public File getFile(Long id) {
        System.out.println("DB get");
        Optional<File> fileOptional = fileRepository.findById(id);
        if (!fileOptional.isPresent()) {
            throw new CustomException("File does not exists");
        }
        return fileOptional.get();
    }

    @CacheEvict(value = "files", allEntries = true)
    public File create(String name, MultipartFile file, Long folderId) throws IOException {
        System.out.println("DB save");
        String username = JwtTokenUtils.getMyUsername();
        User user = userRepository.getByUsername(username);

        int max = Integer.parseInt(configUtility.getProperty("file.max-upload"));
        System.out.println(max);
        if (user.getUploads() >= max)
            throw new CustomException("You have reached the limit for uploading files");

        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (!folderOptional.isPresent())
            throw new CustomException("Folder does not exists");

        Optional<File> fileOptional = fileRepository.findByName(name);
        if (fileOptional.isPresent())
            throw new CustomException("Name has already token");

        String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();
        String path = absolutePath + "\\src\\main\\webapp\\WEB-INF\\uploads\\";
        Long now = Instant.now().getEpochSecond();
        var fileName = now + " - " + file.getOriginalFilename();
        var is = file.getInputStream();

        Files.copy(is, Paths.get(path + fileName),
                StandardCopyOption.REPLACE_EXISTING);
        String PUBLIC_PATH = "uploads/" + fileName;
        File DBFile = new File
                (
                        name,
                        PUBLIC_PATH,
                        /*username,*/
                        false,
                        folderOptional.get(),
                        user
                );
        DBFile.setCreatedAt(new Date());
        fileRepository.save(DBFile);
        int uploads = user.getUploads();
        user.setUploads(++uploads);
        userRepository.save(user);
        return DBFile;
    }

    @Transactional
    @CacheEvict(value = "files", allEntries = true)
    public File update(Long id, String name, MultipartFile file) throws IOException {
        System.out.println("DB update");
        Optional<File> fileOptional = fileRepository.findByName(name);
        File DBFile;
        if (fileOptional.isPresent()) {
            if (fileOptional.get().getId() != id)
                throw new CustomException("Name has already token");
            else
                DBFile = fileOptional.get();
        } else {
            DBFile = fileRepository.findById(id).get();
        }

        String username = JwtTokenUtils.getMyUsername();
        if (DBFile.isStatus()) {
            if (DBFile.getBarrier() != null && !DBFile.getBarrier().equals(username)) {
                throw new CustomException("File with id " + DBFile.getId() + " has already booked");
            }
        }
        if (file != null) {
            java.io.File fileToDelete = new java.io.File("/src/main/webapp/WEB-INF/" + DBFile.getPath());
            boolean success = fileToDelete.delete();
            System.out.println(success);
            String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();
            String path = absolutePath + "\\src\\main\\webapp\\WEB-INF\\uploads\\";
            Long now = Instant.now().getEpochSecond();
            var fileName = now + " - " + file.getOriginalFilename();
            var is = file.getInputStream();

            Files.copy(is, Paths.get(path + fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            String PUBLIC_PATH = "uploads/" + fileName;
            DBFile.setPath(PUBLIC_PATH);
        }
        DBFile.setName(name);
        DBFile.setUpdatedAt(new Date());
        fileRepository.save(DBFile);
        return DBFile;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED,
            propagation = Propagation.REQUIRED,
            readOnly = false,
            timeout = 100,
            rollbackFor = Exception.class)
    public String booking(Long[] filesIds) {
        List<File> files = checkIds(filesIds);
        String username = JwtTokenUtils.getMyUsername();
        for (File file : files) {
            if (file.isStatus()) {
                if (file.getBarrier() != null && !file.getBarrier().equals(username))
                    throw new CustomException("File with id " + file.getId() + " has already booked");
            } else {
                file.setBarrier(username);
                file.setStatus(true);
                file.setBookedAt(new Date());
            }
        }
        fileRepository.saveAll(files);
        return "";
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED,
            propagation = Propagation.REQUIRED,
            readOnly = false,
            timeout = 100,
            rollbackFor = Exception.class)
    public String bookingCancellation(Long[] filesIds) {
        List<File> files = checkIds(filesIds);
        String username = JwtTokenUtils.getMyUsername();
        for (File file : files) {
            if (file.isStatus()) {
                if (file.getBarrier() != null && !file.getBarrier().equals(username))
                    throw new CustomException("File with id " + file.getId() + " has already booked");
                else {
                    file.setStatus(false);
                    file.setBarrier("");
                    file.setUnbookedAt(new Date());
                }
            }
        }
        fileRepository.saveAll(files);
        return "";
    }

    @CacheEvict(value = "files", allEntries = true)
    public String fileDelete(Long id) throws IOException {
        System.out.println("DB delete");
        Optional<File> fileOptional = fileRepository.findById(id);
        if (!fileOptional.isPresent())
            throw new CustomException("File with id " + id + " not found");
        File file = fileOptional.get();
        User user = file.getUser();
        String username = JwtTokenUtils.getMyUsername();
        if (!file.getUser().getUsername().equals(username))
            throw new CustomException("Cannot delete this file");

        if (file.isStatus()) {
            if (file.getBarrier() != null && !file.getBarrier().equals(username)) {
                throw new CustomException("File with id " + file.getId() + " has already booked");
            }
        }
        String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();
        Path path = Paths.get(absolutePath + "/src/main/webapp/WEB-INF/" + file.getPath());
//        java.io.File fileToDelete = new java.io.File("/src/main/webapp/WEB-INF/" + file.getPath());
        Files.delete(path);
//        System.out.println(success);
        int uploads = user.getUploads();
        uploads--;
        user.setUploads(uploads);
        fileRepository.delete(file);
        userRepository.save(user);
        return "";
    }

    public Resource loadFileAsResource(Long id) {
        File file = fileRepository.findById(id).get();
        String fileName = file.getPath().split("/")[1];
        try {
            String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();
            Path fileStorageLocation = Paths.get(absolutePath + "/src/main/webapp/WEB-INF/uploads")
                    .toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new CustomException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new CustomException("File not found " + fileName);
        }
    }

    private List<File> checkIds(Long[] filesIds) {
        Optional<File> fileOptional;
        List<File> files = new ArrayList<>();
        for (Long id : filesIds) {
            fileOptional = fileRepository.findById(id);
            if (!fileOptional.isPresent())
                throw new CustomException("File with id " + id + " not found");
            files.add(fileOptional.get());
        }
        return files;
    }
}
