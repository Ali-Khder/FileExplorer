package com.FileExplorer.dto.foldersAndFiles;

import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;

public class UserFolderDto {

    @NotBlank
    private Long[] usersIds;

    public UserFolderDto() {
    }

    public UserFolderDto(@NotBlank Long[] usersIds) {
        this.usersIds = usersIds;
    }

    public Long[] getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(Long[] usersIds) {
        this.usersIds = usersIds;
    }

    @Override
    public String toString() {
        return "UserFolderDto{" +
                "usersIds=" + Arrays.toString(usersIds) +
                '}';
    }
}
