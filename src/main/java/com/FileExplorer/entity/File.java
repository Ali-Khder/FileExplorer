package com.FileExplorer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", barrier='" + barrier + '\'' +
                ", status=" + status +
                ", folder=" + folder +
                ", user=" + user +
                '}';
    }
}
