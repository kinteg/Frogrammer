package com.kinteg.frogrammer.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kinteg.frogrammer.db.domain.PostTag;

public interface PostTagRepo extends JpaRepository<PostTag, Long> {
}
