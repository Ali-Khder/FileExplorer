package com.FileExplorer.repository;

import com.FileExplorer.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
}
