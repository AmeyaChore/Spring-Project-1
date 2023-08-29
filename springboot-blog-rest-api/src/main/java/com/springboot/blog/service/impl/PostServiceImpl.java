package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptons.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
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
import java.util.stream.Stream;

@Service
public class PostServiceImpl implements PostService {

    //@Autowired  // if there is only one parameter we can omit @Autowired we can use Constructor to inject dependency
    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category=categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category","Id", postDto.getCategoryId()));

        //Convert DTO to Entity
        Post post=mapToEntity(postDto);
        post.setCategory(category);
        Post savedPost=postRepository.save(post);

        //Convert entity to Dto
        PostDto postResponse=mapToDto(savedPost);
        //postResponse.setCategoryId(savedPost.getCategory().getId());
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

        //get Category Object from the Database
        Category category=categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category","Id",postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost=postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long postId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostDto> getPostByCategoryId(long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","Id",categoryId));

        List<Post> listOfPosts=postRepository.findByCategoryId(categoryId);
        //List<PostDto> content= listOfPosts.stream().map(post->mapToDto(post)).collect(Collectors.toList());
        return listOfPosts.stream().map((post)->mapToDto(post))
                .collect(Collectors.toList());
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
