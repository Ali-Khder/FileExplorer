package com.FileExplorer.service;

import com.FileExplorer.entity.File;
import com.FileExplorer.entity.Folder;
import com.FileExplorer.entity.User;
import com.FileExplorer.repository.FileRepository;
import com.FileExplorer.repository.FolderRepository;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Service
public class FileService {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public FileService(JwtTokenUtils jwtTokenUtils,
                       UserRepository userRepository,
                       FileRepository fileRepository, FolderRepository folderRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public File createFile(String name, MultipartFile file, Long folderId) throws IOException {
        String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();
        String path = absolutePath + "\\src\\main\\webapp\\WEB-INF\\uploads\\";
        Long now = Instant.now().getEpochSecond();
        var fileName = now + " - " + file.getOriginalFilename();
        var is = file.getInputStream();

        Files.copy(is, Paths.get(path + fileName),
                StandardCopyOption.REPLACE_EXISTING);
        String PUBLIC_PATH = path + fileName;
        String username = jwtTokenUtils.getMyUsername();
        User user = userRepository.getByUsername(username);
        Folder folder = folderRepository.findById(folderId).get();
        File DBFile = new File(name, PUBLIC_PATH, false, folder, user);
        fileRepository.save(DBFile);
        return DBFile;
    }
}
