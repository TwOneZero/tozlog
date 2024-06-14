package com.tozlog.api.repository.post;

import com.tozlog.api.domain.Post;
import com.tozlog.api.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
