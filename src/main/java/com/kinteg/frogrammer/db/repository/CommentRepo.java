package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Comment;
import com.kinteg.frogrammer.db.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comment p" +
            " where p.id = :id and p.status = :status", nativeQuery = true)
    Optional<Comment> getByIdAndStatus(@Param("id") Long id, @Param("status") String status);

    @Query(value = "select * from comment c" +
            " where c.post_id = :postId and c.depended_id = 0 and c.status = :status", nativeQuery = true)
    Page<Comment> findAllByStatus(Pageable pageable, long postId, String status);

}