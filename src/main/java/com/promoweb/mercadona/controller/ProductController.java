package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.exception.NoProductsFoundException;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Read
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return  ResponseEntity.ok(product);
        } else {
            throw new EntityNotFoundException("Le produit avec l'id : " + id + " n'existe pas");
        }
    }

    //Create
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updateProduct = productService.updateProduct(id, product);
        if (updateProduct != null) {
            return ResponseEntity.ok(updateProduct);
        } else {
            throw new EntityNotFoundException("Le produit avec l'id : " + id + " n'existe pas");
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la récupération des produits: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<Product>> getPromotionalProducts() {
        try {
            List<Product> promotionalProducts = productService.getPromotionalProducts();
            return ResponseEntity.ok(promotionalProducts);
        } catch (NoProductsFoundException e) {

            logger.warn("Exception lors de la récupération des produits en promotion: {}", e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors de la récupération des produits en promotion.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byUser/{user_id}")
    public ResponseEntity<List<Product>> getProductsByAdmin(@PathVariable Long user_id) {
        try {
            List<Product> productsByUser = productService.getProductsByUser(user_id);
            return ResponseEntity.ok(productsByUser);
        } catch (NoProductsFoundException e) {

            logger.warn("Exception lors de la récupération des produits par admin: {}", e.getMessage());
            throw new EntityNotFoundException("L'administrateur avec l'id : " + user_id + " n'existe pas");
            //return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors de la récupération des produits par l'administrateur.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byCategory/{category_id}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long category_id) {
        try {
            List<Product> productsByCategory = productService.getProductsByCategory(category_id);
            return ResponseEntity.ok(productsByCategory);

        } catch (NoProductsFoundException e) {
            logger.warn("Exception lors de la récupération des produits par catégorie: {}", e.getMessage());
            throw new EntityNotFoundException("La category avec l'id : " + category_id + " n'existe pas");
            //return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors de la récupération des produits par catégorie.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
