package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepo extends JpaRepository<Tag, Long> {

    @Query(value = "select * from tag t" +
            " where t.tag_title ilike ('%' || :title || '%')",
            nativeQuery = true)
    Page<Tag> findAllByTitle(Pageable pageable, String title);

    @Query(value = "select distinct on (t.id) * from tag t" +
            " where t.tag_title ilike ('%' || text(:text) || '%')" +
            " or t.description ilike ('%' || text(:text) || '%')",
            nativeQuery = true)
    Page<Tag> findAllMatches(Pageable pageable, String text);

}
