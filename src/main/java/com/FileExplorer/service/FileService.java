package com.FileExplorer.service;

import com.FileExplorer.entity.File;
import com.FileExplorer.entity.Folder;
import com.FileExplorer.entity.User;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.FileRepository;
import com.FileExplorer.repository.FolderRepository;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FileService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public FileService(UserRepository userRepository,
                       FileRepository fileRepository,
                       FolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public Set<File> getAll(Long id) {
        return folderRepository.findById(id).get().getFiles();
    }

    public File getFile(Long id) {
        return fileRepository.findById(id).get();
    }

    public File create(String name, MultipartFile file, Long folderId) throws IOException {

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
        String username = JwtTokenUtils.getMyUsername();
        User user = userRepository.getByUsername(username);
        File DBFile = new File
                (
                        name,
                        PUBLIC_PATH,
                        false,
                        folderOptional.get(),
                        user
                );
        fileRepository.save(DBFile);
        return DBFile;
    }

    @Transactional
    public File update(Long id, String name, MultipartFile file) throws IOException {
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
        fileRepository.save(DBFile);
        return DBFile;
    }

    @Transactional
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
            }
        }
        fileRepository.saveAll(files);
        return "";
    }

    @Transactional
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
                }
            }
        }
        fileRepository.saveAll(files);
        return "";
    }

    public String fileDelete(Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (!fileOptional.isPresent())
            throw new CustomException("File with id " + id + " not found");
        File file = fileOptional.get();
        String username = JwtTokenUtils.getMyUsername();
        if (file.isStatus()) {
            if (file.getBarrier() != null && !file.getBarrier().equals(username)) {
                throw new CustomException("File with id " + file.getId() + " has already booked");
            }
        }
        fileRepository.delete(file);
        return "";
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
