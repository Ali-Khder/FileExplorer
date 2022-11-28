package com.FileExplorer.dto.auth;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class RegisterDto {

    @NotNull
    private String username;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String fullName;

    @NotNull
    private String password;

    public RegisterDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "RegisterDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
