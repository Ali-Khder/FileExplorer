package com.FileExplorer.dto.foldersAndFiles;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class FileDto {

    @NotBlank
    private String name;

    private Long folderId;

    private MultipartFile file;

    public FileDto() {
    }

    public FileDto(String name, Long folderId, MultipartFile file) {
        this.name = name;
        this.folderId = folderId;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        return "FileDto{" +
                "name='" + name + '\'' +
                ", folderId=" + folderId +
                ", file=" + file +
                '}';
    }
}
