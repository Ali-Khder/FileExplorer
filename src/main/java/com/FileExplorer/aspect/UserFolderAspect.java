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

@Aspect
@Component
public class UserFolderAspect {
    private JwtTokenUtils jwtTokenUtils;
    private FolderRepository folderRepository;

    public UserFolderAspect(JwtTokenUtils jwtTokenUtils, FolderRepository folderRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.folderRepository = folderRepository;
    }

    @Before(value = "execution(* com.FileExplorer.service.FolderService.addUsersToFolder(..)) and args(id, usersIds)")
    public void folderAdvice(JoinPoint joinPoint, Long id, Long[] usersIds) {
        String username = jwtTokenUtils.getMyUsername();
        Folder folder = folderRepository.findById(id).get();

        if (!folder.getOwnerName().equals(username)) {
            throw new CustomException("You are not the owner of folder!", HttpStatus.FORBIDDEN);
        }
    }
}
