package com.promoweb.mercadona.service;

import com.promoweb.mercadona.exception.NoProductsFoundException;
import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.repository.ProductRepository;
import com.promoweb.mercadona.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        if (isValidProduct(product)) {
            return productRepository.save(product);
        }
        return null;
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        if (existingProduct != null && isValidProduct(product)) {
            // Eenregistrer dans la base de données
            existingProduct.setLabel(product.getLabel());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrix(product.getPrix());
            existingProduct.setImage(product.getImage());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPromotion(product.getPromotion());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private boolean isValidProduct(Product product) {
        return product != null && product.getPrix() > 0 && categoryExists(product.getCategory());
    }

    private boolean categoryExists(Category category) {
        return category != null && categoryRepository.existsById(category.getId());
    }


    public List<Product> getPromotionalProducts() {
        List<Product> promotionalProducts = productRepository.findByPromotionIsNotNull();
        if (promotionalProducts.isEmpty()) {
            logger.info("Aucun produit trouvé en promotion.");
            throw new NoProductsFoundException("Il n'y a aucun produit en promotion pour le moment.");
        }
        return promotionalProducts;
    }

    public List<Product> getProductsByUser(Long user_id) {
        List<Product> productsByUser = productRepository.findByUserId(user_id);
        if (productsByUser.isEmpty()) {
            throw new NoProductsFoundException("Aucun produit trouvé pour l'administrateur avec l'ID : " + user_id);
        }
        return productsByUser;
    }

    public List<Product> getProductsByCategory(Long category_id) {
        List<Product> productsByCategory = productRepository.findByCategoryId(category_id);
        if (productsByCategory.isEmpty()) {
            throw new NoProductsFoundException("Aucun produit trouvé dans cette catégory: " + category_id);
        }
        return productsByCategory;
    }

    public Page<Product> findProductsWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {
            // Si le mot-clé est vide, récupérez tous les utilisateurs avec pagination
            return productRepository.findAll(pageable);
        } else {
            // Si un mot-clé est fourni, recherchez les utilisateurs par mot-clé avec pagination
            return productRepository.findByCategoryContains(kw, pageable);
        }
    }

}
