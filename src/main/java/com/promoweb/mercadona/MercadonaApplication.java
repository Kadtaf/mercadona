package com.promoweb.mercadona;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class MercadonaApplication {

    public static void main(String[] args) {

        System.out.println("Bonjour tout le monde");
        SpringApplication.run(MercadonaApplication.class, args);
    }

}
