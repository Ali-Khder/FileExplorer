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
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@SpringBootApplication
@Configuration
@EnableWebMvc
public class FileExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileExplorerApplication.class, args);
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("/WEB-INF/uploads/");
        }
    }

//    @Bean
//    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") final DataSource dataSource) {
//        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
//        resourceDatabasePopulator.addScript(new ClassPathResource("/logger.sql"));
//        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
//        dataSourceInitializer.setDataSource(dataSource);
//        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
//        return dataSourceInitializer;
//    }

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
