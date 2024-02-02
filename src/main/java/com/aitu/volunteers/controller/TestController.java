package com.aitu.volunteers.controller;

import com.aitu.volunteers.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@PreAuthorize("hasAnyAuthority('ROLE_USER','APPROLE_Admin')")
public class TestController {

    private final UserService userService;

    @GetMapping("admin")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public String Admin() {
        return "Admin message";
    }

    @GetMapping("defuser")
    public String User() {
        return "User message";
    }

    @GetMapping("/sub-name")
    public String getOidcUserPrincipal(HttpServletRequest httpServletRequest) {
//        System.out.println(httpServletRequest.getUserPrincipal());
        return httpServletRequest.getUserPrincipal().getName();
    }

    @GetMapping("/test")
    public ResponseEntity<?> getUserPrincipal(HttpServletRequest httpServletRequest) {
//        System.out.println(httpServletRequest.getUserPrincipal());
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userService.getUserBySub(httpServletRequest.getUserPrincipal().getName()));
    }

    @GetMapping("/user/{barcode}")
    public ResponseEntity<?> getUserByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(userService.getUserByBarcode(barcode));
    }
}