package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptons.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    //@Autowired  // if there is only one parameter we can omit @Autowired we can use Constructor to inject dependency
    private PostRepository postRepository;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //Convert DTO to Entity
        Post post=new Post();
        post=mapToEntity(postDto);
        Post savedPost=postRepository.save(post);

        //Convert entity to Dto
        PostDto postResponse=new PostDto();
        postResponse=mapToDto(savedPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPost(int pageNo,int pageSize,String sortBy,String sortDir) {

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        //create Pageable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        Page<Post> posts=postRepository.findAll(pageable);

        //get Content of the Page Object
        List<Post> listOfPosts=posts.getContent();

        List<PostDto> content= listOfPosts.stream().map(post->mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long postId) {
        PostDto postDto=mapToDto(postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId)));
        return postDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long postId) {
        //get Post By Id from the Database and if Post does not exists with given id we will return exception
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost=postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long postId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        postRepository.deleteById(postId);
    }


    //Convert PostDto to Post
   private Post mapToEntity(PostDto postDto){

        //user Model mapper to map source as postDto to destination class Post.class
        Post post=mapper.map(postDto,Post.class);


//        Post post=new Post();
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());
        return post;
   }


   //Convert Post to PostDto
   private PostDto mapToDto(Post post){

        //used Model Mapper to map source as post to destination class PostDto.class
        PostDto postDto=mapper.map(post,PostDto.class);

//        PostDto postDto=new PostDto();
//        postDto.setId(post.getId());
//        postDto.setContent(post.getContent());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
        return postDto;
   }
}
