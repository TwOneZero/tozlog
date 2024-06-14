package com.tozlog.api.service;

import com.tozlog.api.domain.Comment;
import com.tozlog.api.exception.comment.CommentNotFound;
import com.tozlog.api.exception.comment.InvalidPassword;
import com.tozlog.api.exception.post.PostNotFound;
import com.tozlog.api.repository.comment.CommentRepository;
import com.tozlog.api.repository.post.PostRepository;
import com.tozlog.api.request.comment.CommentCreate;
import com.tozlog.api.request.comment.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder  passwordEncoder;

    public void write(Long postId, CommentCreate commentCreate) {
        var post =postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        var encodedPassword = passwordEncoder.encode(commentCreate.getPassword());

        var comment = Comment.builder()
                .author(commentCreate.getAuthor())
                .content(commentCreate.getContent())
                .password(encodedPassword)
                .build();
        post.addComment(comment);

    }

    public void delete(Long commentId, CommentDelete commentDelete) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        if (! passwordEncoder.matches(commentDelete.getPassword(), comment.getPassword())) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);

    }
}
