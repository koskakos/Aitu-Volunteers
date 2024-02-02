package com.aitu.volunteers.service;

import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.UserInfo;
import com.aitu.volunteers.model.request.UpdateUserRequest;
import com.aitu.volunteers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public boolean isExistsUserByUserSub(String sub) {
        return userRepository.existsByUserSub(sub);
    }

    public boolean isRegisteredByUserSub(String sub) {
        return getUserBySub(sub).isRegistered();
    }

    public User getUserBySub(String sub) {
        return userRepository.findUserByUserSub(sub).orElseThrow();
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow();
    }
    public User getUserByBarcode(String barcode) {
        return userRepository.findUserByBarcode(barcode).orElseThrow();
    }

    public User createNewUser(JSONObject accessTokenPayload) {
        String barcode = accessTokenPayload.getString("unique_name").split("@")[0];
        String sub = accessTokenPayload.getJSONObject("xms_st").getString("sub");
        User user = User.builder().userSub(sub)
                .barcode(barcode)
                .isRegistered(false)
                .userInfo(new UserInfo())
                .build();
        userRepository.save(user);
        return null;
    }

    private void setUserInfoFromUpdateUserRequest(User user, UpdateUserRequest request) {
        user.setUserInfo(UserInfo.builder()
                        .id(user.getUserInfo().getId())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .gender(request.getGender())
                .IIN(request.getIIN())
                .phone(request.getPhone()).build());
    }

    public User updateUser(User user, UpdateUserRequest request) {
        setUserInfoFromUpdateUserRequest(user, request);
        return userRepository.save(user);
    }

    public User updateUser(String userSub, UpdateUserRequest request) {
        User user = getUserBySub(userSub);
        setUserInfoFromUpdateUserRequest(user, request);
        return userRepository.save(user);
    }

    public String getAuthorizedUserSub() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (String) principal.getClaims().get("sub");
    }

    public User getAuthorizedUser() {
        return getUserBySub(getAuthorizedUserSub());
    }

}
