package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.exception.NoProductsFoundException;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/listProducts")
    public String listAllProducts(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) {

        // Utilisez votre méthode de recherche produits avec pagination
            Page<Product> pageProducts = productService.findProductsWithPagination(kw, PageRequest.of(page, size));

            model.addAttribute("products",  pageProducts.getContent());
            model.addAttribute("pages", new int[pageProducts.getTotalPages()]);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", kw);
            return "/products/catalogue";
    }

    @GetMapping("/")
    public String catalogue(){
        return "redirect:/listProducts";
    }

    @GetMapping("/allProducts")
    public String getAllProducts(Model model) {
        try {
            List<Product> products = productService.getAllProducts();
            model.addAttribute("products", products);
            return "/catalogue";
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la récupération des produits: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
    }

    //Read
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return  product;
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
