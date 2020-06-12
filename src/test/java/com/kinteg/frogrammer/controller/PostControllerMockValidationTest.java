package com.kinteg.frogrammer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.dto.SimpleTagDto;
import com.kinteg.frogrammer.service.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
class PostControllerMockValidationTest {
    
    private final String DEFAULT_URL = "/api/post/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService mockPostService;

    /*
        {
            "timestamp": "11-06-2020 10-58-58",
            "status": 400,
            "errors": {
                "getPost.id": "must be greater than or equal to 1"
            }
        }
     */
    @WithMockUser
    @Test
    void getPostFailedId() throws Exception {

        long id = -1L;

        mockMvc.perform(get(DEFAULT_URL + id))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors", hasKey("getPost.id")))
                .andExpect(jsonPath("$.errors.['getPost.id']",
                        hasToString("must be greater than or equal to 1")))
        ;

        verify(mockPostService, times(0)).getPost(any(Long.class));
    }

    /*
        {
            "id": 1,
            "title": "This is main text",
            "mainText": "This is main text",
            "preview": "This is preview",
            "username": "This is username",
            "tags": [
                {
                    "id": 1,
                    "title": "This is tag title"
                }
            ]
        }
     */
    @WithMockUser
    @Test
    void getPostSuccessful() throws Exception {

        Long id = 1L;

        SimpleTagDto simpleTagDto = SimpleTagDto
                .builder()
                .id(id)
                .title("This is tag title")
                .build();

        SimplePostDto simplePostDto = SimplePostDto
                .builder()
                .id(id)
                .mainText("This is main text")
                .preview("This is preview")
                .tags(Collections.singleton(simpleTagDto))
                .title("This is title")
                .username("This is username")
                .build();

        doReturn(simplePostDto)
                .when(mockPostService).getPost(id);


        mockMvc.perform(get(DEFAULT_URL + id.intValue()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(1)))
                .andExpect(jsonPath("$.tags.[0].id", is(1)))
                .andExpect(jsonPath("$.tags.[0].title", is("This is tag title")))
        ;

        verify(mockPostService, times(1)).getPost(any(Long.class));
    }

    /*
        {
            "timestamp": "11-06-2020 10-58-58",
            "status": 400,
            "errors": {
                "getPost.id": "must be greater than or equal to 1"
            }
        }
     */
    @WithMockUser
    @Test
    void deleteByIdFailedId() throws Exception {

        long id = -1L;

        mockMvc.perform(delete(DEFAULT_URL + id))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("deleteById.id")))
                .andExpect(jsonPath("$.errors.['deleteById.id']",
                        hasToString("must be greater than or equal to 1")))
        ;

        verify(mockPostService, times(0)).delete(any(Long.class));
    }

    /*
        {
        }
     */
    @WithMockUser
    @Test
    void deleteByIdSuccessful() throws Exception {

        long id = 1L;

        mockMvc.perform(delete(DEFAULT_URL + (int) id))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;

        verify(mockPostService, times(1)).delete(any(Long.class));
    }

    /*
        {
            "timestamp": "11-06-2020 01-21-20",
            "status": 400,
            "errors": [
                "Require mainText isn't null.",
                "Require preview isn't null.",
                "Require tags isn't null.",
                "Pleas provide a preview.",
                "Pleas provide a title.",
                "Pleas provide a mainText.",
                "Require title isn't null.",
                "Pleas provide tags."
            ]
        }
     */
    @WithMockUser
    @Test
    void createFailedId() throws Exception {
        mockMvc.perform(post(DEFAULT_URL + "create")
                .content("{}")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors", hasSize(8)))
                .andExpect(jsonPath("$.errors", hasItem("Require mainText isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Require preview isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Require tags isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide a preview.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide a title.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide a mainText.")))
                .andExpect(jsonPath("$.errors", hasItem("Require title isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide tags.")))
        ;

        verify(mockPostService, times(0)).create(any(CreatePostDto.class));
    }

    /*
        {
            "id": 1,
            "title": "This is main text",
            "mainText": "This is main text",
            "preview": "This is preview",
            "username": "This is username",
            "tags": [
                {
                    "id": 1,
                    "title": "This is tag title"
                }
            ]
        }
     */
    @WithMockUser
    @Test
    void createSuccessful() throws Exception {
        Long id = 1L;

        SimpleTagDto simpleTagDto = SimpleTagDto
                .builder()
                .id(id)
                .title("This is tag title")
                .build();

        SimplePostDto simplePostDto = SimplePostDto
                .builder()
                .id(id)
                .mainText("This is main text")
                .preview("This is preview")
                .tags(Collections.singleton(simpleTagDto))
                .title("This is title")
                .username("This is username")
                .build();


        CreatePostDto createPostDto = new CreatePostDto(
                "This is title", "This is main text", "This is preview", Collections.singleton(id));

        doReturn(simplePostDto)
                .when(mockPostService).create(createPostDto);

        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", "This is title");
        userRequest.put("mainText", "This is main text");
        userRequest.put("preview", "This is preview");
        userRequest.put("tags", Collections.singleton(id));

        mockMvc.perform(post(DEFAULT_URL + "create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(1)))
                .andExpect(jsonPath("$.tags.[0].id", is(1)))
                .andExpect(jsonPath("$.tags.[0].title", is("This is tag title")))
        ;

        verify(mockPostService, times(1)).create(createPostDto);
    }

    /*
        {
            "timestamp": "11-06-2020 01-21-20",
            "status": 400,
            "errors": [
                "Require mainText isn't null.",
                "Require preview isn't null.",
                "Require tags isn't null.",
                "Pleas provide a preview.",
                "Pleas provide a title.",
                "Pleas provide a mainText.",
                "Require title isn't null.",
                "Pleas provide tags."
            ]
        }
     */
    @WithMockUser
    @Test
    void updateFailedBody() throws Exception {

        mockMvc.perform(put(DEFAULT_URL + "update/-1")
                .content("{}")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors", hasSize(8)))
                .andExpect(jsonPath("$.errors", hasItem("Require mainText isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Require preview isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Require tags isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide a preview.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide a title.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide a mainText.")))
                .andExpect(jsonPath("$.errors", hasItem("Require title isn't null.")))
                .andExpect(jsonPath("$.errors", hasItem("Pleas provide tags.")))
        ;

        verify(mockPostService, times(0)).create(any(CreatePostDto.class));
    }

    /*
        {
            "timestamp": "11-06-2020 10-58-58",
            "status": 400,
            "update": {
                "getPost.id": "must be greater than or equal to 1"
            }
        }
     */
    @WithMockUser
    @Test
    void updateFailedId() throws Exception {

        Long id = 1L;

        SimpleTagDto simpleTagDto = SimpleTagDto
                .builder()
                .id(id)
                .title("This is tag title")
                .build();

        SimplePostDto simplePostDto = SimplePostDto
                .builder()
                .id(id)
                .mainText("This is main text")
                .preview("This is preview")
                .tags(Collections.singleton(simpleTagDto))
                .title("This is title")
                .username("This is username")
                .build();


        CreatePostDto createPostDto = new CreatePostDto(
                "This is title", "This is main text", "This is preview", Collections.singleton(id));

        doReturn(simplePostDto)
                .when(mockPostService).create(createPostDto);

        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", "This is title");
        userRequest.put("mainText", "This is main text");
        userRequest.put("preview", "This is preview");
        userRequest.put("tags", Collections.singleton(id));

        mockMvc.perform(put(DEFAULT_URL + "update/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors", hasKey("update.id")))
                .andExpect(jsonPath("$.errors.['update.id']",
                        hasToString("must be greater than or equal to 1")))
        ;

        verify(mockPostService, times(0)).update(createPostDto, -1L);
    }

    /*
        {
            "id": 1,
            "title": "This is main text",
            "mainText": "This is main text",
            "preview": "This is preview",
            "username": "This is username",
            "tags": [
                {
                    "id": 1,
                    "title": "This is tag title"
                }
            ]
        }
     */
    @WithMockUser
    @Test
    void updateSuccessful() throws Exception {

        Long id = 1L;

        SimpleTagDto simpleTagDto = SimpleTagDto
                .builder()
                .id(id)
                .title("This is tag title")
                .build();

        SimplePostDto simplePostDto = SimplePostDto
                .builder()
                .id(id)
                .mainText("This is main text")
                .preview("This is preview")
                .tags(Collections.singleton(simpleTagDto))
                .title("This is title")
                .username("This is username")
                .build();


        CreatePostDto createPostDto = new CreatePostDto(
                "This is title", "This is main text", "This is preview", Collections.singleton(id));

        doReturn(simplePostDto)
                .when(mockPostService).update(createPostDto, id);

        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put("title", "This is title");
        userRequest.put("mainText", "This is main text");
        userRequest.put("preview", "This is preview");
        userRequest.put("tags", Collections.singleton(id));

        mockMvc.perform(put(DEFAULT_URL + "update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.title", is("This is title")))
                .andExpect(jsonPath("$.mainText", is("This is main text")))
                .andExpect(jsonPath("$.preview", is("This is preview")))
                .andExpect(jsonPath("$.username", is("This is username")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(1)))
                .andExpect(jsonPath("$.tags.[0].id", is(1)))
                .andExpect(jsonPath("$.tags.[0].title", is("This is tag title")))
        ;

        verify(mockPostService, times(1)).update(createPostDto, id);
    }


}