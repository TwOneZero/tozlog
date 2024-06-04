package com.tozlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tozlog.api.domain.Post;
import com.tozlog.api.repository.PostRepository;
import com.tozlog.api.request.PostCreate;
import com.tozlog.api.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("/posts 요청 성공")
    void test() throws Exception {
        //Given
        PostCreate request = PostCreate.builder()
                .title("글제목입니다.")
                .content("글내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //When
        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
//                        .content("{\"title\" : \"글제목입니다.\", \"content\" : \"글내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
        //Then


    }

    @Test
    @DisplayName("/posts 요청 시 title 값은 필수")
    void test2() throws Exception {
        //Given
        PostCreate request = PostCreate.builder()
                .content("글내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        //When
        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
//                        .content("{\"title\" : null, \"content\" : \"글내용입니다.\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청"))
                .andExpect(jsonPath("$.validation").hasJsonPath())
                .andExpect(jsonPath("$.validation.title").value("글 제목을 제대로 입력해주세요"))
                .andDo(print());
        //Then

    }

    @Test
    @DisplayName("/posts 요청 시 DB에 저장")
    void test3() throws Exception {
        //Given
        PostCreate request = PostCreate.builder()
                .title("글제목입니다")
                .content("글내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        //When
        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
//                        .content("{\"title\" : \"글제목입니다.\", \"content\" : \"글내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andDo(print());
        //Then
        assertEquals(1L, postRepository.count());

    }

    @Test
    @DisplayName("글 단건 조회")
    void test4() throws Exception {
        //Given
        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(newPost);

        //When & Then
        mockMvc.perform(get("/posts/{postId}", newPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newPost.getId()))
                .andExpect(jsonPath("$.title").value(newPost.getTitle()))
                .andExpect(jsonPath("$.content").value(newPost.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 조회 - 여러 개, 내림차순")
    void test5() throws Exception {
        //Given
        Post newPost1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .build());
        Post newPost2 = postRepository.save(Post.builder()
                .title("title_2")
                .content("content_2")
                .build());

        //When & Then
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title").value("title_2"))
                .andExpect(jsonPath("$[0].content").value("content_2"))
                .andExpect(jsonPath("$[1].title").value("title_1"))
                .andExpect(jsonPath("$[1].content").value("content_1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 첫페이지 조회 - 10개 게시물 id 내림차순")
    void test6() throws Exception{
        //Given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder().title("제목 - " + i)
                        .content("내용 - " + i)
                        .build()).toList();
        postRepository.saveAll(requestPosts);
        //When & Then
        mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "1")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("내용 - 30"))
                .andExpect(jsonPath("$[4].title").value("제목 - 26"))
                .andExpect(jsonPath("$[4].content").value("내용 - 26"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 첫페이지 조회 - page을 0 으로 조회해도 첫페이지로 가져옴")
    void test7() throws Exception{
        //Given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder().title("제목 - " + i)
                        .content("내용 - " + i)
                        .build()).toList();
        postRepository.saveAll(requestPosts);
        //When & Then
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("내용 - 30"))
                .andExpect(jsonPath("$[4].title").value("제목 - 26"))
                .andExpect(jsonPath("$[4].content").value("내용 - 26"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test8() throws Exception{
        //Given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("updated foo")
                .content("bar")
                .build();

        //When & Then
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("글 삭제")
    void test9() throws Exception{
        //Given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        //When & Then
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("글 단건 조회 - 존재하지 않는 글 조회")
    void test10() throws Exception {
        //Given
        //When & Then
        mockMvc.perform(get("/posts/{postId}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정 - 존재하지 않는 글 수정")
    void test11() throws Exception{
        //Given
        PostEdit postEdit = PostEdit.builder()
                .title("updated foo")
                .content("bar")
                .build();
        //When & Then
        mockMvc.perform(patch("/posts/{postId}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("글 저장 시 '바보' 가 포함되면 에러를 반환")
    void test12() throws Exception {
        //Given
        PostCreate request = PostCreate.builder()
                .title("저는 바보똥개입니다")
                .content("글내용입니다.")
                .build();

        //When
        //Then
        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }


}