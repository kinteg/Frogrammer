package com.kinteg.frogrammer.service.user.impl;

import com.kinteg.frogrammer.db.domain.Role;
import com.kinteg.frogrammer.db.domain.RoleEntity;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.RoleRepo;
import com.kinteg.frogrammer.db.repository.UserRepo;
import com.kinteg.frogrammer.service.user.UserRegisterService;
import org.aspectj.weaver.BCException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepo userRepo;


    public UserRegisterServiceImpl(RoleRepo roleRepo, BCryptPasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public User register(User user) {
        if (!Objects.equals(user.getPassword(), user.getConfirmPassword())) {
            throw new BCException("Password mismatch.");
        }

        if (userRepo.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new BCException("A user with that username or mail already exists.");
        }

        makeUserForRegister(user);

        return userRepo.save(user);
    }

    private void makeUserForRegister(User user) {
        RoleEntity roleUser = roleRepo.findByName(Role.ROLE_USER.name());
        List<RoleEntity> userRoleEntities = new ArrayList<>(Collections.singletonList(roleUser));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRoleEntities);
        user.setStatus(Status.ACTIVE);
    }

}
