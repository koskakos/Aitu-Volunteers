package com.aitu.volunteers.service;

import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.UserInfo;
import com.aitu.volunteers.model.request.UserRegistrationRequest;
import com.aitu.volunteers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    public User saveUser(String sub, UserRegistrationRequest request) {
        User user = User.builder().userSub(sub)
                .userInfo(UserInfo.builder()
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .gender(request.getGender())
                        .IIN(request.getIIN())
                        .phone(request.getPhone()).build())
                .build();
        return userRepository.save(user);
    }

    public String getAuthorizedUserSub() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  (String) principal.getClaims().get("sub");
    }

}
