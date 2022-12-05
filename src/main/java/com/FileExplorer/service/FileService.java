package com.FileExplorer.service;

import com.FileExplorer.entity.Log;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    @Autowired
    ServletContext context;
    //    private final LogRepository logRepository;
//
//    public FileService(LogRepository logRepository,
//                       JwtTokenUtils jwtTokenUtils) {
//        this.logRepository = logRepository;
//    }

    public String createFile(MultipartFile file) throws IOException {
//        byte[] bytes = file.getBytes();
//        Path path = Paths.get(context.getRealPath("resources/uploads") + file.getOriginalFilename());
//        Files.write(path, bytes);
//        System.out.println(path);
        String absolutePath = new FileSystemResource("").getFile().getAbsolutePath();
        System.out.println(absolutePath);
//        file.transferTo(new File(absolutePath, file.getOriginalFilename()));
        return "";
    }
}
