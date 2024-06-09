package com.tozlog.api.controller;


import com.tozlog.api.config.UserSession;
import com.tozlog.api.request.PostCreate;
import com.tozlog.api.request.PostEdit;
import com.tozlog.api.request.PostSearch;
import com.tozlog.api.response.post.PostResponse;
import com.tozlog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info(">>> {}", userSession.id);
        return userSession.id;
    }


    @PostMapping()
    public void posts(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
    }

    @GetMapping()
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @GetMapping("/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long postId) {
        return postService.get(postId);
    }

    @PatchMapping("/{postId}")
    public void edit(
            @PathVariable(name = "postId") Long postId,
            @RequestBody @Valid PostEdit editRequest
    ) {
        postService.edit(postId, editRequest);
    }

    @DeleteMapping("/{postId}")
    public void delete(@PathVariable(name = "postId") Long postId){
        postService.delete(postId);
    }
}
