package com.aitu.volunteers.controller;

import com.aitu.volunteers.model.request.PostRequest;
import com.aitu.volunteers.service.PostService;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> savePost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.createPost(userService.getAuthorizedUser(), postRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

}
