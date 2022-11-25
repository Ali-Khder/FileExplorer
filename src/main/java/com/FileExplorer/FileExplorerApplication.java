package com.FileExplorer;

import com.FileExplorer.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class FileExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileExplorerApplication.class, args);
    }
}
