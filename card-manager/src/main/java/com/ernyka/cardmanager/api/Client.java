package com.ernyka.cardmanager.api;

import com.ernyka.cardmanager.api.model.Comment;
import com.ernyka.cardmanager.api.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "jplaceholder", url = "https://my-json-server.typicode.com/")
public interface Client {
    @GetMapping("typicode/demo/posts")
    List<Post> getAllPosts();

    @GetMapping("typicode/demo/posts/{id}")
    Post getPostById(@PathVariable("id") Integer id);

    @GetMapping("typicode/demo/comments")
    List<Comment> getAllComments();

    @GetMapping("typicode/demo/comments/{id}")
    Comment getCommentById(@PathVariable("id") Integer id);
}
