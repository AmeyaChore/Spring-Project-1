package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private long id;

    //title should not be null or empty
    //title should have at least 2 character
    @NotEmpty
    @Size(min = 2,message = "Post Title Should Have Al Least 2 Characters")
    private String title;

    //post Description should not be null or Empty
    //post Description should have at least 10 character
    @NotEmpty
    @Size(min = 10,message = "post Description have at least 10 character")
    private String description;

    //Post Content Should Not be Empty or null
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;

    private long categoryId;
}
