package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {
}
