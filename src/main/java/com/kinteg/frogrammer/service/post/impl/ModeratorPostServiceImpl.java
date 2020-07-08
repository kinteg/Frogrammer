package com.kinteg.frogrammer.service.post.impl;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.Tag;
import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.dto.post.CreatePostDto;
import com.kinteg.frogrammer.dto.EventType;
import com.kinteg.frogrammer.dto.ObjectType;
import com.kinteg.frogrammer.dto.post.SearchPostDto;
import com.kinteg.frogrammer.service.post.ModeratorPostService;
import com.kinteg.frogrammer.service.privileges.UserPrivilegesValidator;
import com.kinteg.frogrammer.util.WsSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;

@Service
public class ModeratorPostServiceImpl implements ModeratorPostService {

    private final PostRepo postRepo;
    private final UserPrivilegesValidator privilegesValidator;
    private final BiConsumer<EventType, Post> wsSender;
    private final TagRepo tagRepo;

    public ModeratorPostServiceImpl(
            PostRepo postRepo, UserPrivilegesValidator privilegesValidator,
            WsSender wsSender, TagRepo tagRepo) {
        this.postRepo = postRepo;
        this.privilegesValidator = privilegesValidator;
        this.wsSender = wsSender.getSender(ObjectType.POST, Class.class);
        this.tagRepo = tagRepo;
    }

    @Override
    public Post getPostNotActive(Long id) {
        return postRepo.getByIdAndStatus(id, Status.NOT_ACTIVE.name())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public Page<Post> searchAllNotActive(Pageable pageable) {
        return postRepo.findAllByStatus(pageable, Status.NOT_ACTIVE.name());
    }

    @Override
    public Page<Post> deepSearchNotActive(Pageable pageable, SearchPostDto searchPost) {
        return postRepo.findAllBySearchPostDto(
                pageable, searchPost.fix(), Status.NOT_ACTIVE.name());
    }

    @Override
    public Page<Post> searchByTagNotActive(Pageable pageable, List<Long> tags) {
        tags.add(0L);
        return postRepo.findAllByTags(
                pageable, tags, Status.NOT_ACTIVE.name());
    }

    @Override
    public Page<Post> searchByTextNotActive(Pageable pageable, String searchText) {
        return postRepo.findAllByText(
                pageable, Objects.toString(searchText, ""), Status.NOT_ACTIVE.name());
    }

    @Override
    public void deletePost(Long id) {
        postRepo.findById(id).
                map(post -> {
                    privilegesValidator.checkModeratorPrivileges();

                    if (!Objects.equals(post.getStatus(), Status.ACTIVE) ||
                            !Objects.equals(post.getStatus(), Status.NOT_ACTIVE)) {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id.");
                    }

                    post.setStatus(Status.DELETED);
                    post.setDeletedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                    postRepo.save(post);
                    wsSender.accept(EventType.DELETE, post);

                    return post;
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public void activatePost(Long id) {
        postRepo.findById(id).
                map(post -> {
                    privilegesValidator.checkModeratorPrivileges();

                    if (!Objects.equals(post.getStatus(), Status.NOT_ACTIVE)) {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id.");
                    }

                    post.setStatus(Status.ACTIVE);
                    post.setPublished(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                    postRepo.save(post);
                    wsSender.accept(EventType.DELETE, post);

                    return post;
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public void disablePost(Long id) {
        postRepo.findById(id).
                map(post -> {
                    privilegesValidator.checkModeratorPrivileges();

                    if (!Objects.equals(post.getStatus(), Status.ACTIVE)) {
                        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id.");
                    }

                    post.setStatus(Status.NOT_ACTIVE);
                    post.setPublished(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                    post.setNotActivationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                    postRepo.save(post);
                    wsSender.accept(EventType.DELETE, post);

                    return post;
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public Post update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException {
        return postRepo.findById(id)
                .map(post -> {
                    privilegesValidator.checkUserPrivileges(id);

                    if (!Objects.equals(post.getStatus(), Status.ACTIVE) ||
                            !Objects.equals(post.getStatus(), Status.NOT_ACTIVE)) {
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
        User user = privilegesValidator.getUser();
        Post post = createPostDto.toPost(tags);

        post.setStatus(Status.ACTIVE);
        post.setPublished(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        post.setUser(user);

        postRepo.save(post);
        wsSender.accept(EventType.CREATE, post);

        return post;
    }

}
