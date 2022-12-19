package com.FileExplorer.aspect;

import com.FileExplorer.entity.User;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizeAdminAspect {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public AuthorizeAdminAspect(UserRepository userRepository, JwtTokenUtils jwtTokenUtils) {
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Before(value = "execution(* com.FileExplorer.controller.LogController.allLogs(..))")
    public void loginAdvice(JoinPoint joinPoint) {
        String username = jwtTokenUtils.getMyUsername();
        User user = userRepository
                .findByUsername(username).get();
        boolean authorize;
        String[] roles = user.getRoles().split(",");
        authorize = false;
        for (String role : roles) {
            System.out.println(role);
            if (role.equals("ROLE_ADMIN")) {
                authorize = true;
            }
        }
        if (!authorize)
            throw new CustomException("Operation Not allowed", HttpStatus.FORBIDDEN);
    }
}
