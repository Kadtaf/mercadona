package com.promoweb.mercadona.security;

import com.promoweb.mercadona.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
            auth.requestMatchers("/api/products/catalogue", "/api/products/products", "/api/products/listProducts", "/webjars/**", "/jscript/**", "/assets/**", "/css/**").permitAll();
            auth.requestMatchers("/api/products/savePromotion", "/api/products/editProduct/**", "/api/products/deleteProduct/**", "/api/products/updateProduct/**").hasAnyRole("ADMIN", "SUPER_ADMIN");
            auth.requestMatchers("/api/users/updateUser/**", ":api/users/**", "/api/users/index", "/swagger-ui.html", "/api/users/listUser", "/api/users/updateUser/**").hasAnyRole("ADMIN", "SUPER_ADMIN");
            auth.anyRequest().authenticated();

    }).formLogin(formLoginConfigurer -> formLoginConfigurer
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/api/products/products", true)
                )
      .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/api/products/products")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
      ).build();
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