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
        var user = UserAccount.builder()
                .name("이원영")
                .email("210@mail.com")
                .password("12345")
                .build();
        userAccountRepository.save(user);

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
        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(newPost);

        //When
        PostResponse post = postService.get(newPost.getId());

        //Then
        assertNotNull(post);
        assertEquals("foo",post.getTitle());
        assertEquals("bar",post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회 - 존재하지 않는 글")
    void test8() {
        //Given
        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(newPost);

        //Then
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(newPost.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 여러 개 조회 - 1페이지 조회")
    void test3() {
        //Given
//        final int pageNumber = 0; // 0페이지가 처음임
//        final int pageSize = 5;
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC,"id"));

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder().title("제목 - " + i)
                        .content("내용 - " + i)
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
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
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
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
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
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        //When
        postService.delete(post.getId());
        //Then
        assertEquals(0, postRepository.count());
    }
    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test9() {
        //Given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        //When
        //Then
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

}