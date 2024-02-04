package com.aitu.volunteers.service;

import com.aitu.volunteers.model.*;
import com.aitu.volunteers.model.request.UpdateUserRequest;
import com.aitu.volunteers.model.request.UserBanRequest;
import com.aitu.volunteers.repository.UserBanRepository;
import com.aitu.volunteers.repository.UserCertificateRepository;
import com.aitu.volunteers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final StorageService storageService;

    private final UserBanRepository userBanRepository;

    private final UserCertificateRepository userCertificateRepository;


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
                .kazakh(request.getKazakh())
                .russian(request.getRussian())
                .english(request.getEnglish())
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

    public List<UserBan> getAllUserBan(User user) {
        return user.getBans();
    }

    public UserBan banUser(User user, UserBanRequest userBanRequest) {
        UserBan userBan = UserBan.builder()
                .user(user)
                .reason(userBanRequest.getReason())
                .startDate(userBanRequest.getStartDate())
                .endDate(userBanRequest.getEndDate())
                .build();
        user.getBans().add(userBan);
        userRepository.save(user);
        return userBan;
    }

    public void deleteUserBan(Long banId) {
        userBanRepository.deleteById(banId);
    }

    public boolean hasActiveBan(User user) {
        return userBanRepository.existsByUserAndEndDateAfter(user, LocalDateTime.now());
    }

    public User toggleApproveCertificate(User user) {
        if(user.getCertificate() == null) return null;
        user.getCertificate().setIsApproved(!user.getCertificate().getIsApproved());
        return userRepository.save(user);
    }

    public User deleteCertificate(User user) {
        UserCertificate userCertificate = user.getCertificate();
        if(userCertificate == null) {
            return null;
        }
        storageService.deleteUserCertificate(user);
        userCertificateRepository.delete(userCertificate);
        user.setCertificate(null);
        return userRepository.save(user);
    }

    public User uploadCertificate(User user, MultipartFile certificate) {
        if(user.getCertificate() != null) {
            return null;
        }
        String certificateUrl = storageService.storeUserCertificate(user, certificate);
        if(certificateUrl == null) return null;
        user.setCertificate(UserCertificate.builder()
                    .certificateUrl(certificateUrl)
                    .isApproved(false)
                    .build());
        return userRepository.save(user);
    }

    public User addTeamToUser(User user, Team team) {
        user.setTeam(team);
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
