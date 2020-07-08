package com.kinteg.frogrammer.service.tag.impl;

import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.dto.tag.CreatedTagDto;
import com.kinteg.frogrammer.service.tag.AdminTagService;
import org.springframework.stereotype.Service;

@Service
public class AdminTagServiceImpl implements AdminTagService {

    private final TagRepo tagRepo;

    public AdminTagServiceImpl(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public Tag createTag(CreatedTagDto createdTagDto) {
        return tagRepo.save(createdTagDto.toTag());
    }

    @Override
    public void deleteTag(Long id) {
        tagRepo.deleteById(id);
    }

}
