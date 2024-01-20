package com.aitu.volunteers.config;

import com.aitu.volunteers.model.User;
import com.aitu.volunteers.repository.UserRepository;
import com.aitu.volunteers.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;


@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {


    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sub = (String) principal.getClaims().get("sub");

        if (!userService.isExistsUserByUserSub(sub)) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getOutputStream().println(new ObjectMapper().
                    writeValueAsString("Not registered"));
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth") || path.startsWith("/files") || path.startsWith("/api/v1/post") || path.startsWith("/actuator");
    }
}
