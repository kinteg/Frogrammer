package com.kinteg.frogrammer.controller.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.post.CreatePostDto;
import com.kinteg.frogrammer.dto.post.PostPageDto;
import com.kinteg.frogrammer.dto.post.SearchPostDto;
import com.kinteg.frogrammer.dto.post.SimplePostDto;
import com.kinteg.frogrammer.service.post.ModeratorPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("api/moderator/post")
@Slf4j
@Validated
public class ModeratorPostController {

    private final ModeratorPostService postService;

    public ModeratorPostController(@Qualifier("moderatorPostServiceImpl") ModeratorPostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Cacheable(value = "getPost", key = "#id")
    public ResponseEntity<SimplePostDto> getPost(@NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.ok(SimplePostDto.toSimplePost(postService.getPostNotActive(id)));
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    @Cacheable(value = "getAll", key = "#pageable")
    public ResponseEntity<PostPageDto> getAll(
            @PageableDefault(sort = "not_activation_date", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.searchAllNotActive(pageable)));
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<PostPageDto> search(
            @PageableDefault(sort = "deleted_date", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody SearchPostDto searchPost) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.deepSearchNotActive(pageable, searchPost)));
    }

    @GetMapping(value = "/search/{id}", produces = "application/json")
    public ResponseEntity<PostPageDto> searchByTagId(
            @PageableDefault(sort = "not_activation_date", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable List<Long> id) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.searchByTagNotActive(pageable, id)));
    }

    @GetMapping(value = "/search/simpleSearch", produces = "application/json")
    public ResponseEntity<PostPageDto> searchByText(
            @PageableDefault(sort = "not_activation_date", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String searchText) {
        return ResponseEntity.ok(PostPageDto.toSimplePost(postService.searchByTextNotActive(pageable, searchText)));
    }

    @DeleteMapping(value = "{id}", consumes = "application/json")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/activate/{id}", consumes = "application/json")
    public ResponseEntity<?> activatePost(@PathVariable Long id) {
        postService.activatePost(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/disable/{id}", consumes = "application/json")
    public ResponseEntity<?> disablePost(@PathVariable Long id) {
        postService.disablePost(id);
        return ResponseEntity.ok().build();
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

}
