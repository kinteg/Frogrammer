package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@RestController
@RequestMapping("api/post")
@Slf4j
public class PostController {

    private final PostRepo postRepo;

    public PostController(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Post> getPost(@NotNull @PathVariable long id) {
        if (postRepo.existsById(id)) {
            return new ResponseEntity<>(postRepo.getById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<Page<Post>> getAll(@PageableDefault(sort = "id") Pageable pageable) {
        return new ResponseEntity<>(postRepo.findAll(pageable), HttpStatus.OK);
    }

//    @PostMapping(value = "g", produces = "application/json")
//    public Page<Post> getAllByTitle(@PageableDefault(sort = "id") Pageable pageable, @RequestParam String title) {
//        return postRepo.findAllByTitleLike(pageable, title);
//    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Post> deleteById(@NotNull @PathVariable Long id) {
        if (postRepo.existsById(id)) {
            postRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Post> update(@Valid @RequestBody Post post) {
        if (Objects.nonNull(post.getId()) && postRepo.existsById(post.getId())) {
            return new ResponseEntity<>(postRepo.save(post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {

        if (!postRepo.existsById(post.getId())) {
            return new ResponseEntity<>(postRepo.save(post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
