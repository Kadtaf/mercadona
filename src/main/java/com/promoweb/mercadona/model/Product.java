package com.promoweb.mercadona.model;


import jakarta.persistence.*;



@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String label;
    private String description;
    private double prix;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Product() {
    }

    public Product(String label,
                   String description,
                   double prix,
                   String image,
                   Category category,
                   Promotion promotion
    ) {
        this.label = label;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.category = category;
        this.promotion = promotion;

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "Product{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", image='" + image + '\'' +
                ", category=" + category +
                ", promotion=" + promotion +
                ", user=" + user +
                '}';
    }
}
