package com.kinteg.frogrammer.service;

import com.kinteg.frogrammer.db.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User register(User user);

    Page<User> getAll(Pageable pageable);

    User findByUsername(String username);

    User findById(Long id);

    boolean delete(Long id);

}
