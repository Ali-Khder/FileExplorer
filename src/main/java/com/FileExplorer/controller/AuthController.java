package com.FileExplorer.controller;

import com.FileExplorer.dtos.LoginDto;
import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.tokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
//    @RequestMapping(value = "/login",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> Login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                password
        ));
        LOG.debug("Token for: " + authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token is generated { " + token + " }");
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", token);
        return ResponseHandler.responseBuilder(true, HttpStatus.OK, "Login success", data);
    }
}