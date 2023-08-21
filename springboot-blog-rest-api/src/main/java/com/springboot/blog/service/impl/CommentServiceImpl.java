package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptons.BlogAPIException;
import com.springboot.blog.exceptons.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId,CommentDto commentDto) {
        Comment comment=dtoToEntity(commentDto);

        //retrieve post by id
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));


        //set post to comment entity
        comment.setPost(post);

        //save Comment entity to the database
        Comment savedComment=commentRepository.save(comment);

        return mapToDto(comment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        //retrieve Comments by post Id
        List<Comment> comments=commentRepository.findByPostId(postId);
//        List<CommentDto> commentDtos=new ArrayList<>();
//        comments.forEach((c)->{
//            commentDtos.add(mapToDto(c));
//        });
        //Convert list of Comment entities to list of Comment dto's
        return comments.stream().map(comment->mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId,long commentId) {

        //retrieve post by postId
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //retrieve the Comments by id
        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","Id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment Does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateCommentById(long postId,CommentDto commentDto,long commentId) {
        //retrieve post by PostId
        Post post=postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Id",postId));

        //retrieve comment by Comment Id
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","Id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment Does not belong to post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment=commentRepository.save(comment);
        return mapToDto(updatedComment);


    }

    @Override
    public void deleteComment(long postId, long commentId) {
        //retrieve Post with post id
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //retrieve Comment with comment Id
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","Id",commentId));

        //check whether comment belong to perticular post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment Does Not Belong to Post");
        }
        commentRepository.deleteById(commentId);
    }

    private Comment dtoToEntity(CommentDto commentDto){

        //map commentDto object to Comment.class
        Comment comment=mapper.map(commentDto,Comment.class);

//        Comment comment=new Comment();
//        comment.setBody(commentDto.getBody());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
        return comment;
    }

    private CommentDto mapToDto(Comment comment){
        //map Comment class to CommentDto.class
        CommentDto commentDto=mapper.map(comment,CommentDto.class);


//        CommentDto commentDto=new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setBody(comment.getBody());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
        return commentDto;
    }
}
