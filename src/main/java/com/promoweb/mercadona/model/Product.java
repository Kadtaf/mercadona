package com.promoweb.mercadona.model;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

//@JsonIgnoreProperties({"promotion", "category", "user"})
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NotBlank(message = "Le champ label ne peut pas être vide")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,_!'\" ?À-ÿ-]+$", message = "Le texte doit contenir uniquement des lettres, des chiffres, des espaces, et des lettres avec accent.")
    public String label;

    @NotBlank(message = "Le champ description ne peut pas être vide")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,_!'\" ?À-ÿ-]+$", message = "Le texte doit contenir uniquement des lettres, des chiffres, des espaces, et des lettres avec accent.")
    private String description;

    @NotNull(message = "Le champ prix ne peut pas être nul")
    @DecimalMin(value = "0.01", message = "Le prix doit être un nombre supérieur à zéro")
    private double prix;
    private String imagePath;


    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Product() {
    }

    public Product(String label,
                   String description,
                   double prix,
                   String imagePath,
                   Category category,
                   Promotion promotion,
                   User user
    ) {
        this.label = label;
        this.description = description;
        this.prix = prix;
        this.imagePath = imagePath;
        this.category = category;
        this.promotion = promotion;
        this.user = user;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getPrixPromotion() {

        Promotion promotion = getPromotion();
        if ( promotion !=null) {
            return  prix * (1 - promotion.getDiscountPercentage() / 100);
        }

        return prix;
    }

    public void setPrix(double prix, double discount) {

        if (discount != 0) {
            this.prix = prix * (1 - discount / 100);
        } else {
            this.prix = prix;
        }
    }

    public void setPrix(double prix) {

            this.prix = prix;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
                "id='" + id + '\'' +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", image='" + imagePath + '\'' +
                //", category=" + category +
                //", promotion=" + promotion +
                //", user=" + user +
                '}';
    }

}
