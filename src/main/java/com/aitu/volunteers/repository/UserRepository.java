package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserSub(String sub);

//    boolean existsByEmail(String email);
    boolean existsByUserSub(String userSub);


}
