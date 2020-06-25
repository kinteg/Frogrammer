package com.kinteg.frogrammer.service.post.impl;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.dto.*;
import com.kinteg.frogrammer.service.post.PostService;
import com.kinteg.frogrammer.service.user.UserLoginService;
import com.kinteg.frogrammer.util.WsSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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
        this.wsSender = wsSender.getSender(ObjectType.POST, Class.class);
    }

    @Override
    public Post getPost(Long id) throws HttpClientErrorException {
        return postRepo.getByIdAndStatus(id, Status.ACTIVE.name())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public Page<Post> getAll(Pageable pageable) {
        return postRepo.findAllByStatus(pageable, Status.ACTIVE.name());
    }

    @Override
    public Post update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException {
        return postRepo.findById(id)
                .map(post -> {
                    checkUserPrivileges(id);

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
        tags.add(tagRepo.findById(0L).orElseThrow());
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
                    checkUserPrivileges(id);

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

    @Override
    public Page<Post> search(Pageable pageable, SearchPostDto searchPost) {
        return postRepo.findAllBySearchPostDto(
                pageable, searchPost.fix(), Status.ACTIVE.name());
    }

    @Override
    public Page<Post> searchByTag(Pageable pageable, List<Long> tags) {
        tags.add(0L);
        return postRepo.findAllByTags(
                pageable, tags, Status.ACTIVE.name());
    }

    @Override
    public Page<Post> searchByText(Pageable pageable, String searchText) {
        return postRepo.findAllByText(
                pageable, Objects.toString(searchText, ""), Status.ACTIVE.name());
    }

    private void checkUserPrivileges(Long id) {
        User user = userLoginService.getAuthUser();

        if (!Objects.equals(user.getId(), id)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Invalid user for this resource.");
        }

    }

}
