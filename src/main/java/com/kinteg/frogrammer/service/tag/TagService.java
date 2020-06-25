package com.kinteg.frogrammer.service.tag;

import com.kinteg.frogrammer.db.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TagService {

    Tag getTagById(Long id);

    Page<Tag> getAll(Pageable pageable);

    Page<Tag> searchAllByTitle(Pageable pageable, String title);

    Page<Tag> searchAllByMatches(Pageable pageable, String text);

}
