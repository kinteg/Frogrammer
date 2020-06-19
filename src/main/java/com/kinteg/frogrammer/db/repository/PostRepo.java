package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {

    @Query(value = "select * from post p" +
            " where p.id = :id and p.status = :status", nativeQuery = true)
    Optional<Post> getByIdAndStatus(@Param("id") Long id, @Param("status") String status);

    @Query(value = "select * from post p" +
            " where p.status = :status", nativeQuery = true)
    Page<Post> findAllByStatus(Pageable pageable, String status);

}
