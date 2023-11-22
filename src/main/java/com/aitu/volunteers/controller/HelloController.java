package com.aitu.volunteers.controller;

import com.azure.core.annotation.Get;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@PreAuthorize("hasAnyAuthority('ROLE_USER','APPROLE_Admin')")
public class HelloController {

    @GetMapping("admin")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public String Admin() {
        return "Admin message";
    }

    @GetMapping("user")
    public String User() {
        return "User message";
    }

    @GetMapping("/sub-name")
    public String getOidcUserPrincipal(HttpServletRequest httpServletRequest) {
//        System.out.println(httpServletRequest.getUserPrincipal());
        return httpServletRequest.getUserPrincipal().getName();
    }
}