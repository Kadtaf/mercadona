package com.promoweb.mercadona;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@OpenAPIDefinition
public class MercadonaApplication {

    public static void main(String[] args) {

        System.out.println("Bonjour tout le monde");
        SpringApplication.run(MercadonaApplication.class, args);
    }

}