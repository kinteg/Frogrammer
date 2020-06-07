package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/post")
@Slf4j
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<SimplePostDto> getPost(@NotNull @Min(0) @PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<Page<SimplePostDto>> getAll(@PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(postService.getAll(pageable));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteById(@NotNull @Min(0) @PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Post> update(@Valid @RequestBody Post post) {
        //TODO make post update!
        return null;
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<SimplePostDto> create(@Valid @RequestBody CreatePostDto post) {
        return new ResponseEntity<>(postService.create(post), HttpStatus.CREATED);
    }

}
