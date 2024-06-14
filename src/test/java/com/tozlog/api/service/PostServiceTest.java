package com.tozlog.api.service;

import com.tozlog.api.domain.Post;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.exception.post.PostNotFound;
import com.tozlog.api.repository.post.PostRepository;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.post.PostCreate;
import com.tozlog.api.request.post.PostEdit;
import com.tozlog.api.request.post.PostSearch;
import com.tozlog.api.response.post.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //Given
        var user = createUserAccount();

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //When
        postService.write(user.getId(), postCreate);

        //Then
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("글 단건 조회")
    void test2() {
        //Given
        var user = createUserAccount();
        Post post = createPostWithUser(user);

        //When
        PostResponse response = postService.get(post.getId());

        //Then
        assertNotNull(response);
        assertEquals("foo",response.getTitle());
        assertEquals("bar",response.getContent());
    }

    @Test
    @DisplayName("글 단건 조회 - 존재하지 않는 글")
    void test8() {
        //Given
        var user = createUserAccount();
        Post newPost = createPostWithUser(user);

        //Then
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(newPost.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 여러 개 조회 - 1페이지 조회")
    void test3() {
        //Given
        var user = createUserAccount();
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder().title("제목 - " + i)
                        .content("내용 - " + i)
                        .userAccount(user)
                        .build()).toList();

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
//                .size(10)
                .build();

        //When
        List<PostResponse> response = postService.getList(postSearch);
        //Then
        //page 에 맞는 수만큼 가져오기
//        assertEquals(pageSize, response.size());
        assertEquals(10L, response.size());
        assertEquals("제목 - 19", response.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정 - 업데이트하지 않은 다른 필드의 값도 보내주기")
    void test4() {
        //Given
        var user = createUserAccount();
        var post = createPostWithUser(user);

        PostEdit postEdit = PostEdit.builder()
                .title("updated foo")
                .build();

        //When
        postService.edit(post.getId(), postEdit);

        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFound::new);
        //Then
        assertEquals("updated foo", updatedPost.getTitle());
        assertEquals("bar", updatedPost.getContent());
    }

    @Test
    @DisplayName("글 제목 수정 - 존재하지 않는 글")
    void test10() {
        //Given
        var user = createUserAccount();
        var post = createPostWithUser(user);

        PostEdit postEdit = PostEdit.builder()
                .title("updated foo")
                .build();

        //When
        //Then
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }

    @Test
    @DisplayName("게시글 삭제")
    void test5() {
        //Given
        var user = createUserAccount();
        var post = createPostWithUser(user);

        //When
        postService.delete(post.getId());
        //Then
        assertEquals(0, postRepository.count());
    }
    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test9() {
        //Given
        var user = createUserAccount();
        var post = createPostWithUser(user);

        //When
        //Then
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

    private UserAccount createUserAccount() {
        var user = UserAccount.builder()
                .name("이원영")
                .email("210@mail.com")
                .password("12345")
                .build();
        return userAccountRepository.save(user);
    }

    private Post createPostWithUser(UserAccount user) {
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .userAccount(user)
                .build();
        return postRepository.save(post);
    }
}