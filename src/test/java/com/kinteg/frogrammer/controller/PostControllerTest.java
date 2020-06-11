package com.kinteg.frogrammer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.repository.PostRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs("target/generated-snippets")
@WebMvcTest(PostController.class)
class PostControllerTest {

//    @MockBean
//    private PostRepo postRepo;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final Post defaultPost =
//            new Post(6L, "Qqqqqqqqqqq", "fgdddddddd", "gdrgfddddd", Collections.emptySet());
//
//    private final Post defaultPostNotValid =
//            new Post(6L, "Qqqqqqqq", "fgddddd", "gdrdddd", Collections.emptySet());
//
//    private final String defaultGetPostHttp = "http://localhost:8080/api/post/6";
//    private final String defaultDeleteHttp = "http://localhost:8080/api/post/delete/6";
//    private final String defaultCreateHttp = "http://localhost:8080/api/post/create";
//    private final String defaultUpdateHttp = "http://localhost:8080/api/post/update";
//    private final String defaultGetAll = "http://localhost:8080/api/post/getAll";
//
//    private final List<String> postFields =
//            new ArrayList<>(Arrays.asList("id", "title", "mainText", "preview", "tags"));
//
//    @Test
//    void successfulGetPost() throws Exception {
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).getById(defaultPost.getId());
//
//        doReturn(true)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(get(defaultGetPostHttp))
//                .andDo(print())
//
//                //then
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//
//                //and then
//                .andDo(document(defaultGetPostHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
//                        )
//                ));
//
//        verify(postRepo, times(1)).getById(defaultPost.getId());
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//    }
//
//    @Test
//    void failedGetPost() throws Exception {
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).getById(defaultPost.getId());
//
//        doReturn(false)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(get(defaultGetPostHttp))
//                .andDo(print())
//
//                //then
//                .andExpect(status().isNotFound())
//
//                //and then
//                .andDo(document(defaultGetPostHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint())
//                ));
//
//        verify(postRepo, times(0)).getById(defaultPost.getId());
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//    }
//
//    @Test
//    void testGetAll() throws Exception {
//        Pageable pageable = PageRequest.of(2, 10, Sort.by("id"));
//        Page<Post> page = new PageImpl<>(Collections.emptyList(), pageable, 10);
//
//        //given
//        doReturn(page)
//                .when(postRepo).findAll(pageable);
//
//        //when
//        mockMvc.perform(get(defaultGetAll + "?page=2&size=10"))
//                .andDo(print())
//
//                //then
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        verify(postRepo, times(1)).findAll(page.getPageable());
//    }
//
//    @Test
//    void successfulCreate() throws Exception {
//
//        Map<String, Object> userRequest = new LinkedHashMap<>();
//        userRequest.put(postFields.get(0), defaultPost.getId());
//        userRequest.put(postFields.get(1), defaultPost.getTitle());
//        userRequest.put(postFields.get(2), defaultPost.getMainText());
//        userRequest.put(postFields.get(3), defaultPost.getPreview());
//        userRequest.put(postFields.get(4), defaultPost.getTags());
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).save(defaultPost);
//        doReturn(false)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(post(defaultCreateHttp)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(userRequest))
//        )
//                .andDo(print())
//
//                //then
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//
//                //and then
//                .andDo(document(defaultCreateHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
//                        ),
//                        responseBody(userRequest)
//                ));
//
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//        verify(postRepo, times(1)).save(defaultPost);
//    }
//
//    @Test
//    void failedCreate() throws Exception {
//
//        Map<String, Object> userRequest = new LinkedHashMap<>();
//        userRequest.put(postFields.get(0), defaultPost.getId());
//        userRequest.put(postFields.get(1), defaultPost.getTitle());
//        userRequest.put(postFields.get(2), defaultPost.getMainText());
//        userRequest.put(postFields.get(3), defaultPost.getPreview());
//        userRequest.put(postFields.get(4), defaultPost.getTags());
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).save(defaultPost);
//        doReturn(true)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(post(defaultCreateHttp)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(userRequest))
//        )
//                .andDo(print())
//
//                //then
//                .andExpect(status().isNotFound())
//
//                //and then
//                .andDo(document(defaultCreateHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
//                        ),
//                        responseBody(userRequest)
//                ));
//
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//        verify(postRepo, times(0)).save(defaultPost);
//    }
//
//    @Test
//    void NotValidCreate() throws Exception {
//
//        Map<String, Object> userRequest = new LinkedHashMap<>();
//        userRequest.put(postFields.get(0), defaultPostNotValid.getId());
//        userRequest.put(postFields.get(1), defaultPostNotValid.getTitle());
//        userRequest.put(postFields.get(2), defaultPostNotValid.getMainText());
//        userRequest.put(postFields.get(3), defaultPostNotValid.getPreview());
//        userRequest.put(postFields.get(4), defaultPostNotValid.getTags());
//        //given
//        doReturn(defaultPostNotValid)
//                .when(postRepo).save(defaultPostNotValid);
//        doReturn(false)
//                .when(postRepo).existsById(defaultPostNotValid.getId());
//
//        //when
//        mockMvc.perform(post(defaultCreateHttp)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(userRequest))
//        )
//                .andDo(print())
//
//                //then
//                .andExpect(status().isBadRequest())
//
//                //and then
//                .andDo(document(defaultCreateHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPostNotValid.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPostNotValid.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPostNotValid.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPostNotValid.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPostNotValid.getTags())
//                        ),
//                        responseBody(userRequest)
//                ));
//
//        verify(postRepo, times(0)).existsById(defaultPostNotValid.getId());
//        verify(postRepo, times(0)).save(defaultPostNotValid);
//    }
//
//    @Test
//    void successfulUpdate() throws Exception {
//
//        Map<String, Object> userRequest = new LinkedHashMap<>();
//        userRequest.put(postFields.get(0), defaultPost.getId());
//        userRequest.put(postFields.get(1), defaultPost.getTitle());
//        userRequest.put(postFields.get(2), defaultPost.getMainText());
//        userRequest.put(postFields.get(3), defaultPost.getPreview());
//        userRequest.put(postFields.get(4), defaultPost.getTags());
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).save(defaultPost);
//        doReturn(true)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(post(defaultUpdateHttp)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(userRequest))
//        )
//                .andDo(print())
//
//                //then
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//
//
//                //and then
//                .andDo(document(defaultUpdateHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
//                        ),
//                        responseBody(userRequest)
//                ));
//
//        verify(postRepo, times(1)).save(defaultPost);
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//    }
//
//    @Test
//    void failedUpdate() throws Exception {
//
//        Map<String, Object> userRequest = new LinkedHashMap<>();
//        userRequest.put(postFields.get(0), defaultPost.getId());
//        userRequest.put(postFields.get(1), defaultPost.getTitle());
//        userRequest.put(postFields.get(2), defaultPost.getMainText());
//        userRequest.put(postFields.get(3), defaultPost.getPreview());
//        userRequest.put(postFields.get(4), defaultPost.getTags());
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).save(defaultPost);
//        doReturn(false)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(post(defaultUpdateHttp)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(userRequest))
//        )
//                .andDo(print())
//
//                //then
//                .andExpect(status().isNotFound())
//
//                //and then
//                .andDo(document(defaultUpdateHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
//                        ),
//                        responseBody(userRequest)
//                ));
//
//        verify(postRepo, times(0)).save(defaultPost);
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//    }
//
//    @Test
//    void NotValidUpdate() throws Exception {
//
//        Map<String, Object> userRequest = new LinkedHashMap<>();
//        userRequest.put(postFields.get(0), defaultPostNotValid.getId());
//        userRequest.put(postFields.get(1), defaultPostNotValid.getTitle());
//        userRequest.put(postFields.get(2), defaultPostNotValid.getMainText());
//        userRequest.put(postFields.get(3), defaultPostNotValid.getPreview());
//        userRequest.put(postFields.get(4), defaultPostNotValid.getTags());
//
//        //given
//        doReturn(defaultPostNotValid)
//                .when(postRepo).save(defaultPostNotValid);
//        doReturn(true)
//                .when(postRepo).existsById(defaultPostNotValid.getId());
//
//        //when
//        mockMvc.perform(post(defaultUpdateHttp)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(userRequest))
//        )
//                .andDo(print())
//
//                //then
//                .andExpect(status().isBadRequest())
//
//                //and then
//                .andDo(document(defaultUpdateHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
//                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
//                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
//                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
//                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
//                        ),
//                        responseBody(userRequest)
//                ));
//
//        verify(postRepo, times(0)).save(defaultPostNotValid);
//        verify(postRepo, times(0)).existsById(defaultPostNotValid.getId());
//    }
//
//
//    @Test
//    void successfulDelete() throws Exception {
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).getById(defaultPost.getId());
//
//        doReturn(true)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(delete(defaultDeleteHttp))
//                .andDo(print())
//
//                //then
//                .andExpect(status().isOk())
//
//                //and then
//                .andDo(document(defaultGetPostHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint())
//                ));
//
//        verify(postRepo, times(1)).deleteById(defaultPost.getId());
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//    }
//
//
//    @Test
//    void failedDelete() throws Exception {
//
//        //given
//        doReturn(defaultPost)
//                .when(postRepo).getById(defaultPost.getId());
//
//        doReturn(false)
//                .when(postRepo).existsById(defaultPost.getId());
//
//        //when
//        mockMvc.perform(delete(defaultDeleteHttp))
//                .andDo(print())
//
//                //then
//                .andExpect(status().isNotFound())
//
//                //and then
//                .andDo(document(defaultGetPostHttp,
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint())
//                ));
//
//        verify(postRepo, times(0)).deleteById(defaultPost.getId());
//        verify(postRepo, times(1)).existsById(defaultPost.getId());
//    }
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    void getPost() {
//    }
//
//    @Test
//    void getAll() {
//    }
//
//    @Test
//    void deleteById() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void create() {
//    }
}