package com.FileExplorer.dto.foldersAndFiles;

import jakarta.validation.constraints.NotBlank;

public class FolderDto {

    @NotBlank
    private String name;

    public FolderDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FolderDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
