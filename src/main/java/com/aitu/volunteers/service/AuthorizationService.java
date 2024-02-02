package com.aitu.volunteers.service;

import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.request.UpdateUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;

    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    private String clientSecret;

    @Value("${frontend.uri.redirect}")
    private String redirectUri;

    public ResponseEntity<?> login(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        URI url;
        try {
            url = new URI("https://login.microsoftonline.com/158f15f3-83e0-4906-824c-69bdc50d9d61/oauth2/v2.0/token");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", "8e43eadc-4f43-4434-b074-7dd9b8d46468");
        map.add("redirect_uri", redirectUri);
        map.add("client_secret", clientSecret);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
        firstLoginCheck(response);
        return response;
    }

    private void firstLoginCheck(ResponseEntity<?> microsoftResponse) {
        String accessToken;
        try {
            accessToken = (String) new ObjectMapper().readValue(microsoftResponse.getBody().toString(), new TypeReference<HashMap<String, Object>>() {}).get("access_token");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String[] parts = accessToken.split("\\.");
        JSONObject payload = new JSONObject(new String(Base64.getUrlDecoder().decode(parts[1])));
        String sub = payload.getJSONObject("xms_st").getString("sub");
        if(!userService.isExistsUserByUserSub(sub)) {
            userService.createNewUser(payload);
        }
    }

    public User registration(String userSub, UpdateUserRequest request) {
        User user = userService.getUserBySub(userSub);
        if(user.isRegistered()) return null;
        user.setRegistered(true);
        return userService.updateUser(user, request);
    }
}
