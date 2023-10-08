package com.promoweb.mercadona.model;


import jakarta.persistence.*;

import java.util.List;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category() {
    }

    public Category(Long id, String label) {

        this.label = label;
        this.id =id;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Category{" +
                "label='" + label + '\'' +
                ", products=" + products +
                '}';
    }
}
