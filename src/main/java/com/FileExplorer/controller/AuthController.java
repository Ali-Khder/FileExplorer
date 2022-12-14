package com.FileExplorer.controller;

import com.FileExplorer.dto.auth.LoginDto;
import com.FileExplorer.dto.auth.RegisterDto;
import com.FileExplorer.entity.User;
import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "/api")
public class AuthController {

    @Autowired
    private AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> Login(HttpServletRequest request,
                                        @Valid LoginDto loginDto) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK
                , "Login success", authService.login(loginDto.getUsername(), loginDto.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid RegisterDto registerDto) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK
                , "Register success", authService.register(registerDto));
        //        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                username,
//                password
//        ));
//        String token = authService.generateToken(authentication);
//        LOG.debug("Token is generated { " + token + " }\nNew user is registered");
    }
}