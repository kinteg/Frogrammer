package com.kinteg.frogrammer.service.post.impl;

import com.kinteg.frogrammer.db.domain.*;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.EventType;
import com.kinteg.frogrammer.dto.ObjectType;
import com.kinteg.frogrammer.dto.PostPageDto;
import com.kinteg.frogrammer.service.post.PostService;
import com.kinteg.frogrammer.service.user.UserLoginService;
import com.kinteg.frogrammer.util.WsSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final TagRepo tagRepo;
    private final UserLoginService userLoginService;
    private final BiConsumer<EventType, Post> wsSender;

    public PostServiceImpl(PostRepo postRepo, TagRepo tagRepo, UserLoginService userLoginService, WsSender wsSender) {
        this.postRepo = postRepo;
        this.tagRepo = tagRepo;
        this.userLoginService = userLoginService;
        this.wsSender = wsSender.getSender(ObjectType.POST, Views.FullPost.class);
    }

    @Override
    public Post getPost(Long id) throws HttpClientErrorException {
        return postRepo.getByIdAndStatus(id, Status.ACTIVE.name())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public PostPageDto getAll(Pageable pageable) {
        Page<Post> posts = postRepo.findAllByStatus(pageable, Status.ACTIVE.name());
        return new PostPageDto(
                posts.getContent(),
                posts.getPageable().getPageNumber(),
                posts.getTotalPages(),
                posts.getTotalElements(),
                posts.getSize()
        );
    }

    @Override
    public Post update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException {
        return postRepo.findById(id)
                .map(post -> {
                    User user = userLoginService.getAuthUser();

                    if (!Objects.equals(user.getId(), id)) {
                        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Invalid user for this resource.");
                    }

                    if (!Objects.equals(post.getStatus(), Status.ACTIVE)) {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id.");
                    }

                    post.setTitle(createPostDto.getTitle());
                    post.setMainText(createPostDto.getMainText());
                    post.setPreview(createPostDto.getPreview());
                    post.setTags(new HashSet<>(tagRepo.findAllById(createPostDto.getTags())));

                    postRepo.save(post);
                    wsSender.accept(EventType.UPDATE, post);

                    return post;
                })
                .orElseGet(() -> create(createPostDto));
    }

    @Override
    public Post create(CreatePostDto createPostDto) {
        Set<Tag> tags = new HashSet<>(tagRepo.findAllById(createPostDto.getTags()));

        User user = userLoginService.getAuthUser();
        Post post = createPostDto.toPost(tags);

        post.setStatus(Status.ACTIVE);
        post.setUser(user);

        postRepo.save(post);
        wsSender.accept(EventType.CREATE, post);

        return post;
    }

    @Override
    public void delete(Long id) throws HttpClientErrorException {
        postRepo.findById(id)
                .map(post -> {
                    if (!Objects.equals(post.getStatus(), Status.ACTIVE)) {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id.");
                    }

                    post.setStatus(Status.DELETED);
                    postRepo.save(post);
                    wsSender.accept(EventType.DELETE, post);

                    return post;
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

}
