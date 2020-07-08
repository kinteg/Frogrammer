package com.kinteg.frogrammer.controller.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.post.CreatePostDto;
import com.kinteg.frogrammer.dto.post.SimplePostDto;
import com.kinteg.frogrammer.service.post.UserPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("api/user/post")
@Slf4j
@Validated
public class UserPostController {

    private final UserPostService postService;

    public UserPostController(@Qualifier("userPostServiceImpl") UserPostService postService) {
        this.postService = postService;
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

}
