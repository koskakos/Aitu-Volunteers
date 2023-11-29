package com.aitu.volunteers.service;

import com.aitu.volunteers.model.User;
import com.aitu.volunteers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public boolean isExistsUserByUserSub(String sub) {
        return userRepository.existsByUserSub(sub);
    }

    public User getUser(String sub) {
        return userRepository.findUserByUserSub(sub).orElseThrow();
    }

}
