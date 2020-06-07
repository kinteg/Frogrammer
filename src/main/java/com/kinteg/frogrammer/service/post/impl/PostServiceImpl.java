package com.kinteg.frogrammer.service.post.impl;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.db.repository.UserRepo;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.security.jwt.JwtUser;
import com.kinteg.frogrammer.service.post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;

    public PostServiceImpl(PostRepo postRepo, TagRepo tagRepo, UserRepo userRepo) {
        this.postRepo = postRepo;
        this.tagRepo = tagRepo;
        this.userRepo = userRepo;
    }

    @Override
    public SimplePostDto getPost(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return SimplePostDto.toSimplePost(post);
    }

    @Override
    public Page<SimplePostDto> getAll(Pageable pageable) {
        Page<Post> posts = postRepo.findAll(pageable);
        List<SimplePostDto> content = posts.getContent()
                .stream().map(SimplePostDto::toSimplePost).collect(Collectors.toList());

        Page<SimplePostDto> postDtoPage = new PageImpl<>(content, posts.getPageable(), content.size());

        return postDtoPage;
    }

    @Override
    public SimplePostDto create(CreatePostDto createPostDto) {
        Set<Tag> tags = new HashSet<>(tagRepo.findAllById(createPostDto.getTags()));

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(jwtUser.getId()).orElseThrow(() -> new AuthorizationServiceException(""));
        Post post = createPostDto.toPost(tags);

        post.setStatus(Status.ACTIVE);
        post.setUser(user);

        postRepo.save(post);

        return SimplePostDto.toSimplePost(post);
    }

    @Override
    public void delete(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        post.setStatus(Status.DELETED);

        postRepo.save(post);
    }

}
