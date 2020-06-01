package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.repository.PostRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void getPost() throws Exception {
        //given
        doReturn(new Post(6L, "Qqqqqqqqqq", "fgdddddddd", "gdrgfddddd", Collections.emptySet()))
                .when(postRepo).getById(6);

        doReturn(true)
                .when(postRepo).existsById(6L);

        //when
        mockMvc.perform(get("http://localhost:8080/api/post/6"))
                .andDo(print())

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        //and then
        .andDo(document("http://localhost:8080/api/post/6",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("id").description("6"),
                        fieldWithPath("title").description("Qqqqqqqqqq"),
                        fieldWithPath("mainText").description("fgdddddddd"),
                        fieldWithPath("preview").description("gdrgfddddd"),
                        fieldWithPath("tags").description("[ ]")
                )
                ));
    }
//
//    @Test
//    void getAll() {
//    }
//
//    @Test
//    void getAllByTitle() {
//    }
}