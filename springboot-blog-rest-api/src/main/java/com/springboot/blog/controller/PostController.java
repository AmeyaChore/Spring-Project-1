package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create blog post
    @PreAuthorize("hasRole('ADMIN')")//this means only ADMIN role will able to Create Post API
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get All Posts rest Api
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value="pageNo",defaultValue= AppConstants.DEFAULT_PAGE_NUMBER,required = false ) int pageNo,
            @RequestParam(value="pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required=false) int pageSize,
            @RequestParam(value="sortBy",defaultValue=AppConstants.DEFAULT_SORT_BY,required=false) String sortBy,
            @RequestParam(value="sortDir",defaultValue=AppConstants.DEFAULT_SORT_DIRECTION,required=false) String sortDir
    ){
        return postService.getAllPost(pageNo,pageSize,sortBy,sortDir);
    }

    //get Post by id
    //{id] -URI template variable
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name="id") long postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    //Update post By id
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable("id") long postId){
        PostDto postResponse=postService.updatePost(postDto,postId);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    //delete Post by Id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post with id:-"+postId+" deleted.",HttpStatus.OK);
    }

    //get all posts by category Id
    //http://localhost:8080/api/posts/category/{id}
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable long categoryId){
        List<PostDto> postDtos=postService.getPostByCategoryId(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
