package com.FileExplorer.controller;

import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.FolderService;
import com.FileExplorer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FolderService folderService;

    public UserController(UserService userService, FolderService folderService) {
        this.userService = userService;
        this.folderService = folderService;
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Users", userService.getUsers());
    }

    @GetMapping("/folders/get/{id}")
    public ResponseEntity<Object> getUsersfolders(@PathVariable Long id) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Users", folderService.getUsersFolders(id));
    }
}