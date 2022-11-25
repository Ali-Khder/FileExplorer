package com.FileExplorer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/auth")
public class UserController {

    @GetMapping
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }
}
