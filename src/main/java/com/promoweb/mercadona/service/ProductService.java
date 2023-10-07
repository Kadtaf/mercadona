package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.repository.ProductRepository;
import com.promoweb.mercadona.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

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

}
