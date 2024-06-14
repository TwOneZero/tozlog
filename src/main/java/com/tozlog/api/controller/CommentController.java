package com.tozlog.api.controller;

import com.tozlog.api.request.comment.CommentCreate;
import com.tozlog.api.request.comment.CommentDelete;
import com.tozlog.api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(
            @PathVariable(name = "postId") Long postId,
            @RequestBody @Valid CommentCreate commentCreate
    ) {
        commentService.write(postId, commentCreate);
    }

    @PostMapping("/comments/{commentId}/delete")
    public void delete(
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody @Valid CommentDelete commentDelete
    ){
        commentService.delete(commentId, commentDelete);
    }
}
