package com.tozlog.api.repository.post;

import com.tozlog.api.domain.Post;
import com.tozlog.api.request.post.PostSearch;


import org.springframework.data.domain.Page;

public interface PostRepositoryCustom {

    Page<Post> getList(PostSearch postSearch);
}
