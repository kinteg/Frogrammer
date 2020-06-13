package com.kinteg.frogrammer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinteg.frogrammer.db.domain.*;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.db.repository.TagRepo;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.service.user.UserLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerMockCRUDTest {

    private final String DEFAULT_URL = "/api/post/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepo mockPostRepo;

    @MockBean
    private TagRepo mockTagRepo;

    @MockBean
    private UserLoginService mockUserLoginService;

    @Test
    void getPost_OK() throws Exception {
        Set<Tag> tags = getTagSet();
        User user = getUser();
        Post post = getPost(user, tags);

        when(mockPostRepo.findById(1L)).thenReturn(Optional.of(post));

        mockMvc.perform(get(DEFAULT_URL + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.created", is(notNullValue())))
                .andExpect(jsonPath("$.updated", is(notNullValue())))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(1)))
                .andExpect(jsonPath("$.tags.[0].id", is(1)))
                .andExpect(jsonPath("$.tags.[0].title", is("Simple Title 1")))
        ;

        verify(mockPostRepo, times(1)).findById(any(Long.class));
    }

    @Test
    void getPost_NOT_FOUND() throws Exception {

        when(mockPostRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(DEFAULT_URL + 1))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockPostRepo, times(1)).findById(any(Long.class));
    }

    @Test
    void getAll_OK() throws Exception {
        Set<Tag> tags = getTagSet();
        User user = getUser();
        Post post = getPost(user, tags);

        when(mockPostRepo.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(Collections.singletonList(post))
        );

        mockMvc.perform(get(DEFAULT_URL + "getAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].id", is(1)))
                .andExpect(jsonPath("$.content.[0].title", is("This is title")))
                .andExpect(jsonPath("$.content.[0].mainText", is("This is main text")))
                .andExpect(jsonPath("$.content.[0].preview", is("This is preview")))
                .andExpect(jsonPath("$.content.[0].username", is("This is username")))
                .andExpect(jsonPath("$.content.[0].created", is(notNullValue())))
                .andExpect(jsonPath("$.content.[0].updated", is(notNullValue())))
                .andExpect(jsonPath("$.content.[0].tags").isArray())
                .andExpect(jsonPath("$.content.[0].tags", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].tags.[0].id", is(1)))
                .andExpect(jsonPath("$.content.[0].tags.[0].title", is("Simple Title 1")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(1)))
        ;

        verify(mockPostRepo, times(1)).findAll(any(Pageable.class));
    }

    @WithMockUser
    @Test
    void deleteById_OK() throws Exception {
        Set<Tag> tags = getTagSet();
        User user = getUser();
        Post post = getPost(user, tags);
        Post returnPost = getPost(user, tags);
        returnPost.setStatus(Status.DELETED);

        when(mockPostRepo.findById(1L)).thenReturn(Optional.of(post));
        when(mockPostRepo.save(post)).thenReturn(returnPost);

        mockMvc.perform(delete(DEFAULT_URL + 1))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(mockPostRepo, times(1)).findById(1L);
        verify(mockPostRepo, times(1)).save(post);
    }

    @WithMockUser
    @Test
    void deleteById_NOT_FOUND() throws Exception {
        when(mockPostRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete(DEFAULT_URL + 1))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockPostRepo, times(1)).findById(1L);
        verify(mockPostRepo, times(0)).save(any(Post.class));
    }

    @WithMockUser
    @Test
    void update_OK() throws Exception {
        Long id = 1L;
        Set<Tag> tags = getTagSet();
        User user = getUser();
        Post post = getPost(user, tags);

        Set<Long> tagsId = post.getTags()
                .stream().map(BaseEntity::getId).collect(Collectors.toSet());

        CreatePostDto createPostDto = new CreatePostDto(
                post.getTitle(), post.getMainText(), post.getPreview(), tagsId);

        Post afterSave = createPostDto.toPost(tags);
        afterSave.setUser(user);
        afterSave.setStatus(Status.ACTIVE);
        afterSave.setId(id);

        doReturn(Optional.of(post))
                .when(mockPostRepo).findById(1L);
        doReturn(user)
                .when(mockUserLoginService).getAuthUser();
        doReturn(new ArrayList<>(tags))
                .when(mockTagRepo).findAllById(any());
        doReturn(afterSave)
                .when(mockPostRepo).save(any(Post.class));


        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", post.getTitle());
        userRequest.put("mainText", post.getMainText());
        userRequest.put("preview", post.getPreview());
        userRequest.put("tags", tagsId);

        mockMvc.perform(put("/api/post/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(3)))
        ;

        verify(mockPostRepo, times(1)).findById(id);
        verify(mockTagRepo, times(1)).findAllById(any());
        verify(mockUserLoginService, times(1)).getAuthUser();
        verify(mockPostRepo, times(1)).save(any(Post.class));
    }

    @WithMockUser
    @Test
    void update_failed_create() throws Exception {
        Long id = 1L;
        Set<Tag> tags = getTagSet();
        User user = getUser();
        Post post = getPost(user, tags);

        Set<Long> tagsId = post.getTags()
                .stream().map(BaseEntity::getId).collect(Collectors.toSet());

        CreatePostDto createPostDto = new CreatePostDto(
                post.getTitle(), post.getMainText(), post.getPreview(), tagsId);

        Post afterSave = createPostDto.toPost(tags);
        afterSave.setUser(user);
        afterSave.setStatus(Status.ACTIVE);
        afterSave.setId(id);


        doReturn(Optional.empty())
                .when(mockPostRepo).findById(1L);
        doReturn(user)
                .when(mockUserLoginService).getAuthUser();
        doReturn(new ArrayList<>(tags))
                .when(mockTagRepo).findAllById(any());
        doReturn(afterSave)
                .when(mockPostRepo).save(any(Post.class));


        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", post.getTitle());
        userRequest.put("mainText", post.getMainText());
        userRequest.put("preview", post.getPreview());
        userRequest.put("tags", tagsId);

        mockMvc.perform(put("/api/post/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(3)))
        ;

        verify(mockPostRepo, times(1)).findById(id);
        verify(mockTagRepo, times(1)).findAllById(any());
        verify(mockUserLoginService, times(1)).getAuthUser();
        verify(mockPostRepo, times(1)).save(any(Post.class));
    }

    @WithMockUser
    @Test
    void update_failed_userId() throws Exception {
        Long id = 1L;
        Set<Tag> tags = getTagSet();
        User user = getUser();
        User failedUser = getUser();
        failedUser.setId(2L);
        Post post = getPost(user, tags);

        Set<Long> tagsId = post.getTags()
                .stream().map(BaseEntity::getId).collect(Collectors.toSet());

        CreatePostDto createPostDto = new CreatePostDto(
                post.getTitle(), post.getMainText(), post.getPreview(), tagsId);

        Post afterSave = createPostDto.toPost(tags);
        afterSave.setUser(user);
        afterSave.setStatus(Status.ACTIVE);
        afterSave.setId(id);


        doReturn(Optional.of(post))
                .when(mockPostRepo).findById(1L);
        doReturn(failedUser)
                .when(mockUserLoginService).getAuthUser();

        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", post.getTitle());
        userRequest.put("mainText", post.getMainText());
        userRequest.put("preview", post.getPreview());
        userRequest.put("tags", tagsId);

        mockMvc.perform(put("/api/post/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;

        verify(mockPostRepo, times(1)).findById(id);
        verify(mockUserLoginService, times(1)).getAuthUser();
    }

    @WithMockUser
    @Test
    void create_OK() throws Exception {
        Long id = 1L;
        Set<Tag> tags = getTagSet();
        User user = getUser();
        Post post = getPost(user, tags);

        Set<Long> tagsId = post.getTags()
                .stream().map(BaseEntity::getId).collect(Collectors.toSet());

        CreatePostDto createPostDto = new CreatePostDto(
                post.getTitle(), post.getMainText(), post.getPreview(), tagsId);

        Post afterSave = createPostDto.toPost(tags);
        afterSave.setUser(user);
        afterSave.setStatus(Status.ACTIVE);
        afterSave.setId(id);

        doReturn(new ArrayList<>(tags))
                .when(mockTagRepo).findAllById(any());
        doReturn(user)
                .when(mockUserLoginService).getAuthUser();
        doReturn(afterSave)
                .when(mockPostRepo).save(any(Post.class));


        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", post.getTitle());
        userRequest.put("mainText", post.getMainText());
        userRequest.put("preview", post.getPreview());
        userRequest.put("tags", tagsId);

        mockMvc.perform(post("/api/post/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(3)))
        ;

        verify(mockTagRepo, times(1)).findAllById(any());
        verify(mockUserLoginService, times(1)).getAuthUser();
        verify(mockPostRepo, times(1)).save(any(Post.class));
    }

    private Set<Tag> getTagSet() {
        Set<Tag> tagSet = new HashSet<>();

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setTitle("Simple Title 1");
        tag.setStatus(Status.ACTIVE);
        tagSet.add(tag);

        tag = new Tag();
        tag.setId(2L);
        tag.setTitle("Simple Title 2");
        tag.setStatus(Status.DELETED);
        tagSet.add(tag);

        tag = new Tag();
        tag.setId(3L);
        tag.setTitle("Simple Title 3");
        tag.setStatus(Status.NOT_ACTIVE);
        tagSet.add(tag);

        return tagSet;
    }

    private User getUser() {
        User user = new User();
        user.setUsername("This is username");
        user.setId(1L);

        return user;
    }

    private Post getPost(User user, Set<Tag> tags) {
        Post post = new Post("This is title", "This is main text",
                "This is preview", user, tags);
        post.setId(1L);
        post.setCreated(new Date());
        post.setUpdated(new Date());

        return post;
    }

}