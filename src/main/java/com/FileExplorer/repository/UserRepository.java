package com.FileExplorer.repository;

import com.FileExplorer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUsername(String username);

    //    @Query("SELECT s FROM User s WHERE s.username = ?1")
    Optional<User> findByUsername(String username);

    //    @Query("SELECT s FROM User s WHERE s.email = ?1")
    Optional<User> findByEmail(String email);

    Optional<User> findByPassword(String password);
}
