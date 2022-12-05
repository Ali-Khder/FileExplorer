package com.FileExplorer.service;

import com.FileExplorer.entity.User;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public UserService(UserRepository userRepository, JwtTokenUtils jwtTokenUtils) {
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        String username = jwtTokenUtils.getMyUsername();
        User current = userRepository.findByUsername(username).get();
        users.remove(current);
        return users;
    }
}
