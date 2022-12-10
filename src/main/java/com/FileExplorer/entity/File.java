package com.FileExplorer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class File {
    @Id
    @SequenceGenerator(
            name = "file_sequence",
            sequenceName = "file_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_sequence"
    )
    private Long id;
    private String name;
    private String path;
    private String barrier;
    private boolean status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date bookedAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date unbookedAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "files")
//    private Set<User> users = new HashSet<>();

    public File() {
    }

    public File(String s) {
    }

    public File(String name, String path, boolean status, Folder folder, User user) {
        this.name = name;
        this.path = path;
        this.status = status;
        this.folder = folder;
        this.user = user;
    }

    public File(String name, String path, String barrier, boolean status, Folder folder, User user) {
        this.name = name;
        this.path = path;
        this.barrier = barrier;
        this.status = status;
        this.folder = folder;
        this.user = user;
    }

    public String getBarrier() {
        return barrier;
    }

    public void setBarrier(String barrier) {
        this.barrier = barrier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(Date bookedAt) {
        this.bookedAt = bookedAt;
    }

    public Date getUnbookedAt() {
        return unbookedAt;
    }

    public void setUnbookedAt(Date unbookedAt) {
        this.unbookedAt = unbookedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", barrier='" + barrier + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", bookedAt=" + bookedAt +
                ", unbookedAt=" + unbookedAt +
                ", updatedAt=" + updatedAt +
                ", folder=" + folder +
                ", user=" + user +
                '}';
    }
}
