package com.tozlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tozlog.api.config.TozlogMockUser;
import com.tozlog.api.domain.Post;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.exception.post.UserNotFound;
import com.tozlog.api.repository.post.PostRepository;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.post.PostCreate;
import com.tozlog.api.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired
    private UserAccountRepository userAccountRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userAccountRepository.deleteAll();
    }


    @Test
    @TozlogMockUser()
    @DisplayName("글 작성 - 성공")
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
    @TozlogMockUser()
    @DisplayName("글 작성 시 title 값은 필수")
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
    @TozlogMockUser()
    @DisplayName("글 작성 시 DB에 저장")
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
        var user = createUserAccount();

        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .userAccount(user)
                .build();
        postRepository.save(newPost);

        //When & Then
        mockMvc.perform(get("/posts/{postId}", newPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(newPost.getId()))
                .andExpect(jsonPath("$.title").value(newPost.getTitle()))
                .andExpect(jsonPath("$.content").value(newPost.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 조회 - 여러 개, 내림차순")
    void test5() throws Exception {
        //Given
        var user = createUserAccount();
        postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .userAccount(user)
                .build());
        postRepository.save(Post.builder()
                .title("title_2")
                .content("content_2")
                .userAccount(user)
                .build());

        //When & Then
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalCount").value(2))
                // items 배열의 길이 검사
                .andExpect(jsonPath("$.items.length()", is(2)))
                // 첫 번째 아이템 검사
                .andExpect(jsonPath("$.items[0].title").value("title_2"))
                .andExpect(jsonPath("$.items[0].content").value("content_2"))
                // 두 번째 아이템 검사
                .andExpect(jsonPath("$.items[1].title").value("title_1"))
                .andExpect(jsonPath("$.items[1].content").value("content_1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 첫페이지 조회 - 10개 게시물 id 내림차순")
    void test6() throws Exception {
        //Given
        var user = createUserAccount();
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder().title("제목 - " + i)
                        .content("내용 - " + i)
                        .userAccount(user)
                        .build()).toList();
        postRepository.saveAll(requestPosts);
        //When & Then
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalCount").value(30))
                // items 배열의 길이 검사
                .andExpect(jsonPath("$.items.length()", is(10)))
                .andDo(print());

    }

    @Test
    @TozlogMockUser()
    @DisplayName("글 제목 수정")
    void test8() throws Exception {
        //Given
        UserAccount mockUser = userAccountRepository.findByEmail("210@mail.com")
                .orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .userAccount(mockUser)
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
    @TozlogMockUser()
    @DisplayName("글 삭제")
    void test9() throws Exception {
        //Given
        UserAccount mockUser = userAccountRepository.findByEmail("210@mail.com")
                .orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .userAccount(mockUser)
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
    @TozlogMockUser()
    @DisplayName("글 제목 수정 - 존재하지 않는 글 수정")
    void test11() throws Exception {
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

    private UserAccount createUserAccount() {
        return userAccountRepository.save(
                UserAccount.builder()
                        .email("210@mail.com")
                        .name("이원영")
                        .password("12345")
                        .build()
        );
    }

}