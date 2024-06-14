package com.tozlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tozlog.api.config.TozlogMockUser;
import com.tozlog.api.domain.Comment;
import com.tozlog.api.domain.Post;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.repository.comment.CommentRepository;
import com.tozlog.api.repository.post.PostRepository;
import com.tozlog.api.request.comment.CommentCreate;
import com.tozlog.api.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userAccountRepository.deleteAll();
    }


    @Test
    @TozlogMockUser()
    @DisplayName("댓글 작성 - 성공")
    void test() throws Exception {
        //Given
        var user = createUserAccount();

        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .userAccount(user)
                .build();
        postRepository.save(newPost);

        CommentCreate commentCreate = CommentCreate.builder()
                .author("원맨")
                .content("댓글 이렇게 쓰는 거 맞냐")
                .password("12345")
                .build();

        String json = objectMapper.writeValueAsString(commentCreate);

        //When
        mockMvc.perform(post("/posts/{postId}/comments", newPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //Then

        assertEquals(1, commentRepository.count());
        var comment = commentRepository.findAll().get(0);
        assertEquals("원맨", comment.getAuthor());
        assertTrue(passwordEncoder.matches("12345", comment.getPassword()));
        assertEquals("댓글 이렇게 쓰는 거 맞냐", comment.getContent());

    }

    @Test
    @TozlogMockUser()
    @DisplayName("댓글 삭제 - 성공")
    void test2() throws Exception {
        //Given
        var user = createUserAccount();

        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .userAccount(user)
                .build();
        postRepository.save(post);

        var encodedPassword = passwordEncoder.encode("12345");

        Comment comment = Comment.builder()
                .author("원맨")
                .content("이거 댓글 이렇게 쓰는 거 맞나요.")
                .password(encodedPassword)
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete commentDelete = CommentDelete.builder().password("12345").build();

        String json = objectMapper.writeValueAsString(commentDelete);
        //expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
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