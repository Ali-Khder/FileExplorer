package com.FileExplorer.service;

import com.FileExplorer.entity.Folder;
import com.FileExplorer.entity.User;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.FolderRepository;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public FolderService(FolderRepository folderRepository,
                         UserRepository userRepository, JwtTokenUtils jwtTokenUtils) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public List<Folder> getFolders() {
        String username = JwtTokenUtils.getMyUsername();
        User user = userRepository.findByUsername(username).get();
        String[] roles = user.getRoles().split(",");
        for (String role : roles) {
            System.out.println(role);
            if (role.equals("ROLE_ADMIN")) {
                return folderRepository.findAll();
            }
        }
        List<Folder> folders = new ArrayList<>();
        Folder publicFolder = folderRepository.findById(1L).get();
        List<Folder> ownered = folderRepository.findByOwnerName(username);
        folders.add(publicFolder);
        folders.addAll(user.getFolders());
        folders.addAll(ownered);
        return folders;
    }

    public Set<User> getFolderUsers(Long id) {
        Folder folder = folderRepository.findById(id).get();
        return folder.getUsers();
    }

    public Set<Folder> getUsersFolders(Long id) {
        Set<Folder> folders = userRepository.findById(id).get().getFolders();
        return folders;
    }

    @Transactional
    public Folder createFolder(String name) {
        String ownerName = jwtTokenUtils.getMyUsername();
        Folder folder = new Folder(name, ownerName);
        folderRepository.save(folder);
        return folder;
    }

    @Transactional
    public String folderAddUsers(Long id, Long[] usersIds) {
        Optional<User> userOptional;

        for (Long userid : usersIds) {
            userOptional = userRepository.findById(userid);
            if (!userOptional.isPresent())
                throw new CustomException("User with id " + userid + " not found");
        }

        User user;
        Set<User> users;
        Set<Folder> folders;
        Folder folder = folderRepository.findById(id).get();

        for (Long userid : usersIds) {
            user = userRepository.findById(userid).get();

            folders = user.getFolders();
            folders.add(folder);
            user.setFolders(folders);

            users = folder.getUsers();
            users.add(user);
            folder.setUsers(users);

            userRepository.save(user);
            folderRepository.save(folder);
        }
        return "";
    }

    @Transactional
    public String folderdeleteUsers(Long id, Long[] usersIds) {
        Optional<User> userOptional;

        for (Long userid : usersIds) {
            userOptional = userRepository.findById(userid);
            if (!userOptional.isPresent())
                throw new CustomException("User with id " + userid + " not found");
        }

        User user;
        Set<User> users;
        Set<Folder> folders;
        Folder folder = folderRepository.findById(id).get();

        for (Long userid : usersIds) {
            user = userRepository.findById(userid).get();

            folders = user.getFolders();
            folders.remove(folder);
            user.setFolders(folders);

            users = folder.getUsers();
            users.remove(user);
            folder.setUsers(users);

            userRepository.save(user);
            folderRepository.save(folder);
        }
        return "";
    }

    public String folderDelete(Long id, Long... usersIds) {
        folderRepository.deleteById(id);
        return "";
    }
}
