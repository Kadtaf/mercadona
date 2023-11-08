package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.repository.CategoryRepository;
import com.promoweb.mercadona.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;


    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category getCategoryById(Long id) {
        System.out.println("id category:" + id);
        return categoryRepository.findById(id).orElse(null);
    }

    public Category createCategory(Category category) {
        if (isValidCategory(category)) {
            return categoryRepository.save(category);
        }
        return null;
    }

    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        if (existingCategory != null && isValidCategory(category)) {
            // Eenregistrer dans la base de données
            existingCategory.setLabel(category.getLabel());
            return categoryRepository.save(existingCategory);
        }
        return null;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    private boolean isValidCategory(Category category) {

        return category != null;
    }

    private boolean productExists(Product product) {
        return product != null && productRepository.existsById(product.getId());
    }

    public void saveCategory(Category newCategory) {
        categoryRepository.save(newCategory);
    }


}
