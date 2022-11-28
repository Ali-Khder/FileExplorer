package com.FileExplorer.service;

import com.FileExplorer.entity.User;
import com.FileExplorer.entity.UserAuth;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final JwtEncoder encoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtEncoder jwtEncoder, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.encoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateToken(String username) {
        User user = userRepository.getByUsername(username);
        if (user == null)
            return "Ali";
        UserAuth auth = new UserAuth(user);
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(username)
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

    public String login(String username, String password) {

        String token = generateToken(username);
        System.out.println(this.passwordEncoder.encode(password));
        return token;
    }

    @Transactional
    public Map<String, Object> register(
            String fullName,
            String username,
            String email,
            String password) {
        Optional<User> checkEmail = userRepository
                .findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException("email taken");
        }

        Optional<User> checkUsername = userRepository
                .findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException("username taken");
        }

        User user = new User(fullName, username, email, passwordEncoder.encode(password), "ROLE_USER");
        userRepository.save(user);
        String token = generateToken(username);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("accessToken", token);
        return data;
    }
}
