package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//we don't have to write @Repository because JpaRepository interface will be implemented by SimpleJpaRepository Class which is annotated by @Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(long postId);
}
