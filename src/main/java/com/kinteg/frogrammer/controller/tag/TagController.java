package com.kinteg.frogrammer.controller.tag;

import com.kinteg.frogrammer.dto.tag.SimpleTagDto;
import com.kinteg.frogrammer.dto.tag.TagPageDto;
import com.kinteg.frogrammer.service.tag.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/tag")
@Slf4j
public class TagController {

    private final TagService tagService;

    public TagController(@Qualifier("tagServiceImpl") TagService tagService) {
        this.tagService = tagService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.POST,
                        HttpMethod.PUT, HttpMethod.DELETE,
                        HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Cacheable(value = "getPost", key = "#id")
    public ResponseEntity<SimpleTagDto> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(SimpleTagDto.toSimpleTag(tagService.getTagById(id)));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.HEAD)
    @Cacheable(value = "existPost", key = "#id")
    public ResponseEntity<?> existById(@NotNull @Min(1) @PathVariable Long id) {
        tagService.getTagById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    @Cacheable(value = "getAll", key = "#pageable")
    public ResponseEntity<TagPageDto> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(TagPageDto.toSimpleTag(tagService.getAll(pageable)));
    }

    @GetMapping(value = "/search/searchAllByTitle", produces = "application/json")
    public ResponseEntity<TagPageDto> searchAllByTitle(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String title) {
        return ResponseEntity.ok(TagPageDto.toSimpleTag(tagService.searchAllByTitle(pageable, title)));
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<TagPageDto> searchAllByMatches(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String searchText) {
        return ResponseEntity.ok(TagPageDto.toSimpleTag(tagService.searchAllByMatches(pageable, searchText)));
    }

}