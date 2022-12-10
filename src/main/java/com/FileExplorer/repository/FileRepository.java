package com.FileExplorer.repository;

import com.FileExplorer.entity.File;
import com.FileExplorer.interfaces.selectedOnlyForReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
    List<selectedOnlyForReports> findAllByOrderByCreatedAtDesc();
}
