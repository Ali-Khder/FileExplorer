package com.FileExplorer.controller;

import com.FileExplorer.handler.ResponseHandler;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/auth")
public class UserController {

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Object> home(Principal principal) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK,
                "Hello, " + principal.getName(), null);
    }
}