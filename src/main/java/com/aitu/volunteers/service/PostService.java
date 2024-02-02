package com.aitu.volunteers.service;

import com.aitu.volunteers.model.Post;
import com.aitu.volunteers.model.PostInfo;
import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.request.PostRequest;
import com.aitu.volunteers.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(User user, PostRequest postRequest) {
        Post post = Post.builder()
                .author(user)
                .postInfo(PostInfo.builder()
                        .title(postRequest.getTitle())
                        .description(postRequest.getDescription())
                        .createDate(LocalDateTime.now())
                        .html(postRequest.getHtml())
                        .build())
                .build();
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findPostById(id).orElseThrow(
                () -> (new NoSuchElementException())
        );
    }
}
