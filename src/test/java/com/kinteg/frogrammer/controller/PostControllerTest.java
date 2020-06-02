package com.kinteg.frogrammer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.repository.PostRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs("target/generated-snippets")
@WebMvcTest(PostController.class)
class PostControllerTest {

    @MockBean
    private PostRepo postRepo;

    @Autowired
    private MockMvc mockMvc;

    private final Post defaultPost =
            new Post(6L, "Qqqqqqqqqq", "fgdddddddd", "gdrgfddddd", Collections.emptySet());

    private final String defaultGetPostHttp = "http://localhost:8080/api/post/6";
    private final String defaultCreateHttp = "http://localhost:8080/api/post/create";

    private final List<String> postFields =
            new ArrayList<>(Arrays.asList("id", "title", "mainText", "preview", "tags"));

    @Test
    void getPost() throws Exception {

        //given
        doReturn(defaultPost)
                .when(postRepo).getById(defaultPost.getId());

        doReturn(true)
                .when(postRepo).existsById(defaultPost.getId());

        //when
        mockMvc.perform(get(defaultGetPostHttp))
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //and then
                .andDo(document(defaultGetPostHttp,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
                        )
                ));

        verify(postRepo, times(1)).getById(defaultPost.getId());
        verify(postRepo, times(1)).existsById(defaultPost.getId());
    }
//
//    @Test
//    void getAll() {
//    }
//
//    @Test
//    void getAllByTitle() {
//    }

    @Test
    void create() throws Exception {

        Map<String, Object> userRequest = new LinkedHashMap<>();
        userRequest.put(postFields.get(0), defaultPost.getId());
        userRequest.put(postFields.get(1), defaultPost.getTitle());
        userRequest.put(postFields.get(2), defaultPost.getMainText());
        userRequest.put(postFields.get(3), defaultPost.getPreview());
        userRequest.put(postFields.get(4), defaultPost.getTags());
        //given
        doReturn(defaultPost)
                .when(postRepo).save(defaultPost);

        //when
        mockMvc.perform(post(defaultCreateHttp)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest))
        )
                .andDo(print())

                //then
                .andExpect(status().isOk())

                //and then
                .andDo(document(defaultCreateHttp,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath(postFields.get(0)).description(defaultPost.getId()),
                                fieldWithPath(postFields.get(1)).description(defaultPost.getTitle()),
                                fieldWithPath(postFields.get(2)).description(defaultPost.getMainText()),
                                fieldWithPath(postFields.get(3)).description(defaultPost.getPreview()),
                                fieldWithPath(postFields.get(4)).description(defaultPost.getTags())
                        )
                ));

        verify(postRepo, times(1)).save(defaultPost);
    }

}