package com.FileExplorer;

import com.FileExplorer.entity.User;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.RsaKeyProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class FileExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileExplorerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            userRepository.save(new User(
                    1L,
                    "Ali Khder",
                    "ali_khder",
                    "ali.khder@gmail.com",
                    encoder.encode("1234578"),
                    "ROLE_USER,ROLE_ADMIN"
            ));
        };
    }
}
