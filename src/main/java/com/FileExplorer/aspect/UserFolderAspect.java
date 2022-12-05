package com.FileExplorer.aspect;

import com.FileExplorer.entity.Folder;
import com.FileExplorer.handler.CustomException;
import com.FileExplorer.repository.FolderRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class UserFolderAspect {
    private JwtTokenUtils jwtTokenUtils;
    private FolderRepository folderRepository;

    public UserFolderAspect(JwtTokenUtils jwtTokenUtils, FolderRepository folderRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.folderRepository = folderRepository;
    }

    @Before(value = "execution(* com.FileExplorer.service.FolderService.folder*(..)) and args(id, usersIds)")
    public void folderAdvice(JoinPoint joinPoint, Long id, Long[] usersIds) {
        String username = jwtTokenUtils.getMyUsername();
        Optional<Folder> folder = folderRepository.findById(id);

        if (!folder.isPresent())
            throw new CustomException("Folder not found!");

        if (folder.get().getId() == 1)
            throw new CustomException("Folder is public for all users!");

        if (!folder.get().getOwnerName().equals(username))
            throw new CustomException("You are not the owner of folder!", HttpStatus.FORBIDDEN);
    }
}
