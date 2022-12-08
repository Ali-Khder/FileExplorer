package com.FileExplorer.controller;

import com.FileExplorer.dto.foldersAndFiles.FileDto;
import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<Object> createFile(@Valid FileDto fileDto) throws IOException {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK
                , "File created", fileService.createFile(
                        fileDto.getName(),
                        fileDto.getFile(),
                        fileDto.getFolderId()
                ));

    }
}
