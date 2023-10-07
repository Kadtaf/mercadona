package com.promoweb.mercadona.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String label;
    private String description;
    private double prix;
    private String image;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinTable(
            name = "admins_produits",
            joinColumns = @JoinColumn(name = "produit_id"),
            inverseJoinColumns = @JoinColumn(name = "admins_id")
    )
    private List<Admin> admins;

    public Produit() {
    }

    public Produit(String label, String description, double prix, String image, Category category, Promotion promotion) {
        this.label = label;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.category = category;
        this.promotion = promotion;
    }

    
}
