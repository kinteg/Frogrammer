package com.kinteg.frogrammer.service.tag;

import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.dto.tag.CreatedTagDto;
import org.springframework.stereotype.Service;

@Service
public interface AdminTagService {

    Tag createTag(CreatedTagDto createdTagDto);

    void deleteTag(Long id);

}
