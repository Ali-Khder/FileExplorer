package com.FileExplorer.security;

import com.FileExplorer.entity.User;
import com.FileExplorer.entity.UserAuth;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws CustomException {
        return userRepository
                .findByUsername(username)
                .map(UserAuth::new)
                .orElseThrow(() -> new CustomException("Username not found"));
    }
}
