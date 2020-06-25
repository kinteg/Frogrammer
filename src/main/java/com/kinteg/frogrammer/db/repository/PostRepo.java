package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.dto.SearchPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepo extends JpaRepository<Post, Long> {

    @Query(value = "select * from post p" +
            " where p.id = :id and p.status = :status", nativeQuery = true)
    Optional<Post> getByIdAndStatus(@Param("id") Long id, @Param("status") String status);

    @Query(value = "select * from post p" +
            " where p.status = :status", nativeQuery = true)
    Page<Post> findAllByStatus(Pageable pageable, String status);

    @Query(value = "select distinct on (p.id) * from post p" +
            " right join (select * from post_tag pt where pt.tag_id in :tags) pt on p.id = pt.post_id" +
            " inner join tag t on pt.tag_id = t.id" +
            " where p.status = :status"
            , nativeQuery = true)
    Page<Post> findAllByTags(Pageable pageable, Collection<Long> tags, String status);

    @Query(value = "select distinct on (p.id) * from post p" +
            " where p.status = :status" +
            " and (p.post_title ilike ('%' || :text || '%')" +
            " or p.main_text ilike ('%' || :text || '%')" +
            " or p.preview ilike ('%' || :text || '%'))" +
            "", nativeQuery = true)
    Page<Post> findAllByText(Pageable pageable, String text, String status);

    @Query(value = "select distinct on (p.id) * from post p" +
            " right join (select * from post_tag pt where pt.tag_id in :#{#searchPostDto.tags}) pt on p.id = pt.post_id" +
            " inner join tag t on pt.tag_id = t.id" +
            " where p.status = :status" +
            " and (p.post_title ilike ('%' || text(:#{#searchPostDto.title}) || '%')" +
            " and p.main_text ilike ('%' || text(:#{#searchPostDto.mainText}) || '%')" +
            " and p.preview ilike ('%' || text(:#{#searchPostDto.preview}) || '%'))" +
            "", nativeQuery = true)
    Page<Post> findAllBySearchPostDto(Pageable pageable, @Param("searchPostDto") SearchPostDto searchPostDto, String status);


}
