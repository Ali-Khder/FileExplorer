package com.FileExplorer.controller;

import com.FileExplorer.dto.foldersAndFiles.FolderDto;
import com.FileExplorer.dto.foldersAndFiles.UserFolderDto;
import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping
    public ResponseEntity<Object> getFolders() {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Folders", folderService.getFolders());
    }

    @GetMapping("/users/get/{id}")
    public ResponseEntity<Object> getFolderUsers(@PathVariable Long id) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Folders", folderService.getFolderUsers(id));
    }

    @PostMapping
    public ResponseEntity<Object> createFolder(FolderDto folderDto) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Folder created successfully", folderService.createFolder(folderDto.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> addUsersToFolder(
            @PathVariable Long id,
            UserFolderDto userFolderDto) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Folder updated successfully", folderService.folderAddUsers(id, userFolderDto.getUsersIds()));
    }

    @PutMapping("/users/delete/{id}")
    public ResponseEntity<Object> deleteUsersFromFolder(
            @PathVariable Long id,
            UserFolderDto userFolderDto) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Folder updated successfully", folderService.folderdeleteUsers(id, userFolderDto.getUsersIds()));
    }
}
