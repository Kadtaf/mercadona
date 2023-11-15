package com.promoweb.mercadona.model;



import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private double discountPercentage;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "promotion")
    private List<Product> products;



    public Promotion(Long id, LocalDate startDate, LocalDate endDate, double discountPercentage, List<Product> products) {
        this.id =id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;
        this.products = products;

    }

    public Promotion(LocalDate startDate, LocalDate endDate, double discountPercentage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;

    }

    public Promotion() {

    }


    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "Id=" + id +
                ",startDate=" + startDate +
                ", endDate=" + endDate +
                ", discountPercentage=" + discountPercentage +
                '}';
    }


}