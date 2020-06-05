package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    boolean existsByUsername(String username);

}
