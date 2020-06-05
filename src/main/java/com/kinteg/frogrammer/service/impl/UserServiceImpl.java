package com.kinteg.frogrammer.service.impl;

import com.kinteg.frogrammer.db.domain.Role;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.RoleRepo;
import com.kinteg.frogrammer.db.repository.UserRepo;
import com.kinteg.frogrammer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepo.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        return userRepo.save(user);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);

            return true;
        } else {
            return false;
        }
    }

}
