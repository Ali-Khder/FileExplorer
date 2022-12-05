package com.FileExplorer.repository;

import com.FileExplorer.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Set<Folder> findByOwnerName(String ownerName);
}
