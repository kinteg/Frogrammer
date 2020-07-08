package com.kinteg.frogrammer.service.user.impl;

import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.UserRepo;
import com.kinteg.frogrammer.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(
            UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void delete(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setStatus(Status.DELETED);

        userRepo.save(user);
    }

}
