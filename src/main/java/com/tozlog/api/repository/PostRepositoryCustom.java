package com.tozlog.api.repository;

import com.tozlog.api.domain.Post;
import com.tozlog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
