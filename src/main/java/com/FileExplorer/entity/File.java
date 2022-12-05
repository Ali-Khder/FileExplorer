package com.FileExplorer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Folder {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;
    private String name;
    private String ownerName;

    @JsonIgnore
    @ManyToMany(mappedBy = "folders")
    private Set<User> users = new HashSet<>();

    public Folder() {
    }

    public Folder(String name, String ownerName) {
        this.name = name;
        this.ownerName = ownerName;
    }

    public Folder(String name, String ownerName, Set<User> users) {
        this.name = name;
        this.ownerName = ownerName;
        this.users = users;
    }

    public Folder(Long id, String name, String ownerName, Set<User> users) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.users = users;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", users=" + users +
                '}';
    }
}
