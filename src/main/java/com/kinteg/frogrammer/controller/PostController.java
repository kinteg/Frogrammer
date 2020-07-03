package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.PostPageDto;
import com.kinteg.frogrammer.dto.SearchPostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/post")
@Slf4j
@Validated
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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
    public ResponseEntity<SimplePostDto> getPost(@NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.ok(SimplePostDto.toSimplePost(postService.getPost(id)));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.HEAD)
    @Cacheable(value = "existPost", key = "#id")
    public ResponseEntity<?> existById(@NotNull @Min(1) @PathVariable Long id) {
        postService.getPost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @CacheEvict(value = {"getAll", "existPost", "getPost"}, key = "#id")
    public ResponseEntity<?> deleteById(@NotNull @Min(1) @PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @Caching(
            put = {
                    @CachePut(value = {"existPost", "getPost"}, key = "#id")
            },
            evict = {
                    @CacheEvict(value = "getAll")
            }
    )
    public ResponseEntity<SimplePostDto> update(
            @Valid @RequestBody CreatePostDto createPostDto,
            @NotNull @Min(1) @PathVariable Long id) {

        Post post = postService.update(createPostDto, id);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri).body(SimplePostDto.toSimplePost(post));
    }

    @PostMapping(value = "/create", consumes = "application/json")
    @Caching(
            evict = {
                    @CacheEvict(value = "getAll")
            }
    )
    public ResponseEntity<SimplePostDto> create(@Valid @RequestBody CreatePostDto createPostDto) {
        Post post = postService.create(createPostDto);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri).body(SimplePostDto.toSimplePost(post));
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    @Cacheable(value = "getAll", key = "#pageable")
    public ResponseEntity<PostPageDto> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.getAll(pageable)));
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<PostPageDto> search(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody SearchPostDto searchPost) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.search(pageable, searchPost)));
    }

    @GetMapping(value = "/search/{id}", produces = "application/json")
    public ResponseEntity<PostPageDto> searchByTagId(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable List<Long> id) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.searchByTag(pageable, id)));
    }

    @GetMapping(value = "/search/simpleSearch", produces = "application/json")
    public ResponseEntity<PostPageDto> searchByText(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String searchText) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.searchByText(pageable, searchText)));
    }

}
