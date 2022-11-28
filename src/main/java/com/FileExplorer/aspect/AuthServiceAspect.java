package com.FileExplorer.aspect;

import com.FileExplorer.entity.User;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class AuthServiceAspect {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceAspect(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Before(value = "execution(* com.FileExplorer.service.AuthService.login(..)) and args(username, password)")
    public void loginAdvice(JoinPoint joinPoint, String username, String password) {
        Optional<User> checkUsername = userRepository
                .findByUsername(username);
        if (!checkUsername.isPresent()) {
            throw new CustomException("Login failed, user not found");
        }

        User user = userRepository
                .getByUsername(username);
        boolean result = passwordEncoder.matches(password, user.getPassword());
        if (!result) {
            throw new CustomException("Login failed, password incorrect!");
        }
    }
}
