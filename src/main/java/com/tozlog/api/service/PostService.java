package com.tozlog.api.service;


import com.tozlog.api.domain.Post;
import com.tozlog.api.exception.post.PostNotFound;
import com.tozlog.api.repository.PostRepository;
import com.tozlog.api.request.PostCreate;
import com.tozlog.api.request.PostEdit;
import com.tozlog.api.request.PostSearch;
import com.tozlog.api.response.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = postCreate.toPostEntity();
        postRepository.save(post);
    }

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        return post.toResponse();
    }


    // 글이 너무 많은 경우 -> 비용이 너무 많이 듦
    // Paging 기능으로 나눠서 조회해야 함
    public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
        return postRepository.getList(postSearch).stream()
                .map(Post::toResponse)
                .collect(Collectors.toList());
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
