package com.aitu.volunteers.config;

import com.aitu.volunteers.repository.UserRepository;
import com.aitu.volunteers.service.UserService;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AadOAuth2ResourceServerSecurityConfig {

    /**
     * Add configuration logic as needed.
     */

    private final UserService userService;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(withDefaults()).
                authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**", "/error", "/api/v1/user/confirmemail", "/login", "/files/**")
                        .permitAll().anyRequest().authenticated())
                .addFilterAfter(new CustomFilter(userService), BasicAuthenticationFilter.class).
                apply(AadResourceServerHttpSecurityConfigurer.aadResourceServer());
        return http.build();
    }
}