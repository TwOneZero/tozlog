package com.tozlog.api.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.tozlog.api.config.UserPrincipal;
import com.tozlog.api.request.post.*;
import com.tozlog.api.response.PagingResponse;
import com.tozlog.api.response.post.PostResponse;
import com.tozlog.api.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public void posts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid PostCreate request
    ) {
        postService.write(userPrincipal.getUserId(), request);
    }

    @GetMapping()
    public PagingResponse<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @GetMapping("/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long postId) {
        return postService.get(postId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{postId}")
    public void edit(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "postId") Long postId,
            @RequestBody @Valid PostEdit editRequest
    ) {
        postService.edit(postId, editRequest);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/{postId}")
    public void delete(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "postId") Long postId
    ) {
        postService.delete(postId);
    }
}
