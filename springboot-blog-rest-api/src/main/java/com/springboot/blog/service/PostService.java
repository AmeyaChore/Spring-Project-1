package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    PostResponse getAllPost(int pageNo, int pageSize,String sortBy,String sortDir);

    PostDto getPostById(long postId);

    PostDto updatePost(PostDto postDto,long postId);

    void deletePost(long postId);
}
