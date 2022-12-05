package com.FileExplorer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
    private boolean status;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "files")
//    private Set<User> users = new HashSet<>();

    public File() {
    }
}
