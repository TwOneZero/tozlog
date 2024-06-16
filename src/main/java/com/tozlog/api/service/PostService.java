package com.tozlog.api.service;


import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tozlog.api.domain.Post;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.exception.post.PostNotFound;
import com.tozlog.api.exception.post.UserNotFound;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.repository.post.PostRepository;
import com.tozlog.api.request.post.*;
import com.tozlog.api.response.PagingResponse;
import com.tozlog.api.response.post.PostResponse;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserAccountRepository userAccountRepository;


    public void write(Long userId, PostCreate postCreate) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .userAccount(user)
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        return post.toResponse();
    }


    // 글이 너무 많은 경우 -> 비용이 너무 많이 듦
    // Paging 기능으로 나눠서 조회해야 함
    public PagingResponse<PostResponse> getList(PostSearch postSearch) {
        Page<Post> postPage = postRepository.getList(postSearch);
        log.info(">>>>postPage : {}", postPage);
        PagingResponse<PostResponse> postList = new PagingResponse<>(postPage, PostResponse.class);
        return postList;
    }

    @Transactional
    public void edit(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        var postEditorBuilder = post.toEditor();

        var postEditor = postEditorBuilder
                .title(postEdit.getTitle() == null ? post.getTitle() : postEdit.getTitle())
                .content(postEdit.getContent() == null ? post.getContent() : postEdit.getContent())
                .build();

        post.edit(postEditor);

    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
