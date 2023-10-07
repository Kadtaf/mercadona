package com.promoweb.mercadona.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "admins")
    private List<Product> products;

    public Admin() {
    }

    public Admin(String username, String password, List<Product> products) {
        this.username = username;
        this.password = password;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", products=" + products +
                '}';
    }
}
