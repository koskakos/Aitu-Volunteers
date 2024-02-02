package com.aitu.volunteers.controller;

import com.aitu.volunteers.model.request.UpdateUserRequest;
import com.aitu.volunteers.service.AuthorizationService;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserService userService;
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String code) {
        return authorizationService.login(code);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(authorizationService.registration(userService.getAuthorizedUserSub(), request));
    }
}
