package com.kinteg.frogrammer.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kinteg.frogrammer.db.domain.Tag;

public interface TagRepo extends JpaRepository<Tag, Long> {
}
