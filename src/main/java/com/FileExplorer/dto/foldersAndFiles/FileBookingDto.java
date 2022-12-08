package com.FileExplorer.dto.foldersAndFiles;

import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;

public class FileBookingDto {

    @NotBlank
    private Long[] filesIds;

    public FileBookingDto() {
    }

    public Long[] getFilesIds() {
        return filesIds;
    }

    public void setFilesIds(Long[] filesIds) {
        this.filesIds = filesIds;
    }

    @Override
    public String toString() {
        return "FileBookingDto{" +
                "filesIds=" + Arrays.toString(filesIds) +
                '}';
    }
}
