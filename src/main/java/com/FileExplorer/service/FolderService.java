package com.FileExplorer.service;

import com.FileExplorer.entity.Folder;
import com.FileExplorer.entity.User;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.FolderRepository;
import com.FileExplorer.repository.UserRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Set<Folder> getFolders() {
        String username = jwtTokenUtils.getMyUsername();
        User user = userRepository.findByUsername(username).get();
        Set<Folder> ownered = folderRepository.findByOwnerName(username);
//        Set<Folder> folders = user.getFolders();
        return ownered;
    }

    public Set<User> getFolderUsers(Long id) {
        Folder folder = folderRepository.findById(id).get();
        return folder.getUsers();
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
}
