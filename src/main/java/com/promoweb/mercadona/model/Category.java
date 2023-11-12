package com.promoweb.mercadona.model;


import jakarta.persistence.*;
import org.hibernate.Session;
import org.springframework.data.jpa.provider.HibernateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @Column(name ="status")
    private Boolean status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(Long id, String label, Boolean status) {
        this.id =id;
        this.label = label;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Category{" +
                "label='" + label + '\'' +
                "products='" + products + '\'' +
                '}';
    }


}