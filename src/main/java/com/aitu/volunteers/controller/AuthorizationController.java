package com.aitu.volunteers.controller;

import com.aitu.volunteers.model.request.UserRegistrationRequest;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;

    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    private String clientSecret;

    @Value("${frontend.uri}")
    private String redirectUri;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String code) throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        URI url = new URI("https://login.microsoftonline.com/158f15f3-83e0-4906-824c-69bdc50d9d61/oauth2/v2.0/token");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", "8e43eadc-4f43-4434-b074-7dd9b8d46468");
        map.add("redirect_uri", redirectUri);
        map.add("client_secret", clientSecret);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.saveUser(userService.getAuthorizedUserSub(), request));
    }
}
