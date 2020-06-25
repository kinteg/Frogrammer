package com.kinteg.frogrammer.service.tag.impl;

import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.service.tag.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;

    public TagServiceImpl(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepo.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed tag id."));
    }

    @Override
    public Page<Tag> getAll(Pageable pageable) {
        return tagRepo.findAll(pageable);
    }

    @Override
    public Page<Tag> searchAllByTitle(Pageable pageable, String title) {
        return tagRepo.findAllByTitle(pageable, title);
    }

    @Override
    public Page<Tag> searchAllByMatches(Pageable pageable, String text) {
        return tagRepo.findAllMatches(pageable, text);
    }

}
