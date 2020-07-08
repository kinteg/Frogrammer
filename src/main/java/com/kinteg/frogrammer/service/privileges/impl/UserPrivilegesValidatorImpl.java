package com.kinteg.frogrammer.service.privileges.impl;

import com.kinteg.frogrammer.db.domain.Role;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.service.privileges.UserPrivilegesValidator;
import com.kinteg.frogrammer.service.user.UserLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Service
public class UserPrivilegesValidatorImpl implements UserPrivilegesValidator {

    private final UserLoginService userLoginService;

    public UserPrivilegesValidatorImpl(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @Override
    public User getUser() {
        return userLoginService.getAuthUser();
    }

    @Override
    public boolean checkAuthPrivileges() {
        if (!isUser() || !isModerator() || !isAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Invalid user for this resource.");
        }
        return true;
    }

    @Override
    public boolean checkUserPrivileges(Long id) {
        if (!isValidUser(id) || !isModerator() || !isAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Invalid user for this resource.");
        }
        return true;
    }

    @Override
    public boolean checkModeratorPrivileges() {
        if (!isModerator() || !isAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Invalid user for this resource.");
        }
        return true;
    }

    @Override
    public boolean checkAdministratorPrivileges() {
        if (!isAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Invalid user for this resource.");
        }
        return true;
    }

    private boolean isValidUser(Long id) {
        User user = userLoginService.getAuthUser();
        return Objects.equals(user.getId(), id);
    }

    private boolean isUser() {
        User user = userLoginService.getAuthUser();
        return 1 == user.getRole().stream()
                .filter(roleEntity -> Objects.equals(roleEntity.getName(), Role.ROLE_USER.name()))
                .count();
    }

    private boolean isModerator() {
        User user = userLoginService.getAuthUser();
        return 1 == user.getRole().stream()
                .filter(roleEntity -> Objects.equals(roleEntity.getName(), Role.ROLE_MODERATOR.name()))
                .count();
    }

    private boolean isAdmin() {
        User user = userLoginService.getAuthUser();
        return 1 == user.getRole().stream()
                .filter(roleEntity -> Objects.equals(roleEntity.getName(), Role.ROLE_ADMIN.name()))
                .count();
    }

}
