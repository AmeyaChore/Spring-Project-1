package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name="id") long postId,
                                                    @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{id}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(name="id") long postId){

        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(name="postId") long postId,
                                     @PathVariable(name="commentId") long commentId){

        CommentDto commentDto=commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    @PutMapping("/posts/{pId}/comments/{cId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name="pId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto,
                                                    @PathVariable(name="cId") long commentId){

        CommentDto updatedComment=commentService.updateCommentById(postId,commentDto,commentId);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{pId}/comments/{cId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name="pId") long postId,
                                @PathVariable(name="cId") long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment with id:-"+commentId+" deleted.",HttpStatus.OK);
    }
}
