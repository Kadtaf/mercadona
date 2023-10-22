package com.promoweb.mercadona.security;

import com.promoweb.mercadona.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;


    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.GET, "/api/users/promotions/listPromotion", "/api/users/categories/listCategory", "/webjars/**", "/resources/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/api/users/categories/**", "/api/users/products/**", "/api/users/listUser", "/api/users/promotions/**", "/api/users/updateUser/**").hasAnyRole("ADMIN", "SUPER_ADMIN");
            auth.requestMatchers(HttpMethod.PUT, "/api/users/categories/**","/api/users/updateUser/**", "/api/users/products/**", "/api/users/index", "/api/users/promotions/**").hasAnyRole("ADMIN", "SUPER_ADMIN");
            auth.requestMatchers(HttpMethod.GET, "/api/users/formUser/**", "/api/users/listUser/**", "/api/users/updateUser/**", "/swagger-ui.html").hasAnyRole("ADMIN", "SUPER_ADMIN");
            auth.anyRequest().authenticated();
        }).formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }





}