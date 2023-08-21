package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private long id;

    //name should not be null or Empty
    @NotEmpty(message = "name should not be null or empty")
    private String name;

    //email should not be null or Empty
    @NotEmpty(message = "Email should not be null or Empty")
    @Email
    private String email;

    //comment body should not be null or empty
    //comment body must be minimum 10 Character
    @NotEmpty
    @Size(min = 10,message = "Comment body must be minimum 10 Character")
    private String body;
}
