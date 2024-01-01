package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostById(Long id);
}
