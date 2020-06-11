package com.kinteg.frogrammer.service.post.impl;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.service.post.PostService;
import com.kinteg.frogrammer.service.user.UserLoginService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final TagRepo tagRepo;
    private final UserLoginService userLoginService;

    public PostServiceImpl(PostRepo postRepo, TagRepo tagRepo, UserLoginService userLoginService) {
        this.postRepo = postRepo;
        this.tagRepo = tagRepo;
        this.userLoginService = userLoginService;
    }

    @Override
    public SimplePostDto getPost(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return SimplePostDto.toSimplePost(post);
    }

    @Override
    public Page<SimplePostDto> getAll(Pageable pageable) {
        return postRepo.findAll(pageable).map(SimplePostDto::toSimplePost);
    }

    @Override
    public SimplePostDto create(CreatePostDto createPostDto) {
        Set<Tag> tags = new HashSet<>(tagRepo.findAllById(createPostDto.getTags()));
        User user = userLoginService.getAuthUser();
        Post post = createPostDto.toPost(tags);

        post.setStatus(Status.ACTIVE);
        post.setUser(user);

        postRepo.save(post);

        return SimplePostDto.toSimplePost(post);
    }

    @Override
    public SimplePostDto update(CreatePostDto createPostDto, Long id) {
        return postRepo.findById(id)
                .map(post -> {
                    User user = userLoginService.getAuthUser();

                    if (!Objects.equals(user.getId(), id)) {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
                    }

                    post.setTitle(createPostDto.getTitle());
                    post.setTitle(createPostDto.getTitle());
                    post.setTitle(createPostDto.getTitle());
                    post.setTitle(createPostDto.getTitle());

                    postRepo.save(post);

                    return SimplePostDto.toSimplePost(post);
                })
                .orElseGet(() -> create(createPostDto));
    }

    @Override
    public void delete(Long id) {
        postRepo.findById(id)
                .map(post -> {
                    post.setStatus(Status.DELETED);

                    return postRepo.save(post);
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

}
