package com.FileExplorer.controller;

import com.FileExplorer.dtos.LoginDto;
import com.FileExplorer.service.tokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private tokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(tokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String token(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.username(),
                loginDto.password()
        ));
        LOG.debug("Token for: " + authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token is generated { " + token + " }");
        return token;
    }
}
