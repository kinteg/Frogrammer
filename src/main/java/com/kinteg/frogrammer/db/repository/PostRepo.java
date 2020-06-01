package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {

    Post findById(long id);

    Post getById(long id);

    Page<Post> findAllByTitleLike(Pageable pageable, String title);

}
