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
import com.kinteg.frogrammer.service.post.UserPostService;
import com.kinteg.frogrammer.service.privileges.UserPrivilegesValidator;
import com.kinteg.frogrammer.util.WsSender;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
public class UserPostServiceImpl implements UserPostService {

    private final PostRepo postRepo;
    private final TagRepo tagRepo;
    private final BiConsumer<EventType, Post> wsSender;
    private final UserPrivilegesValidator privilegesValidator;

    public UserPostServiceImpl(PostRepo postRepo, TagRepo tagRepo, WsSender wsSender, UserPrivilegesValidator privilegesValidator) {
        this.postRepo = postRepo;
        this.tagRepo = tagRepo;
        this.wsSender = wsSender.getSender(ObjectType.POST, Class.class);
        this.privilegesValidator = privilegesValidator;
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

        post.setStatus(Status.NOT_ACTIVE);
        post.setUser(user);
        post.setNotActivationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        postRepo.save(post);
        wsSender.accept(EventType.CREATE, post);

        return post;
    }

    @Override
    public void delete(Long id) throws HttpClientErrorException {
        postRepo.findById(id)
                .map(post -> {
                    privilegesValidator.checkUserPrivileges(id);

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

}
