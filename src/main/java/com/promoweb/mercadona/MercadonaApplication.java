package com.promoweb.mercadona;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication

public class MercadonaApplication {

    public static void main(String[] args) {

        System.out.println("Bonjour tout le monde");
        SpringApplication.run(MercadonaApplication.class, args);
    }

}