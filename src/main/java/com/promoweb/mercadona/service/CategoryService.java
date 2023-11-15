package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.repository.CategoryRepository;
import com.promoweb.mercadona.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(Long id) {
        System.out.println("id category:" + id);
        return categoryRepository.findById(id).orElse(null);
    }

    public void createCategory(Category category) {

        categoryRepository.save(category);
    }

    public void updateCategory(Category category) {

            categoryRepository.save(category);
    }

    public void setStatusCategory(Category category) throws Exception{

        List<Product> products = category.getProducts();
        if (!products.isEmpty() && !category.getStatus()) {
            throw new Exception("Cette catégorie est lié à des produits, elle ne peut pas être désactivée");
        } else {
            updateCategory(category);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findByStatus(true);
    }

    public void saveCategory(Category newCategory) {

        categoryRepository.save(newCategory);
    }

    public Page<Category> findCategoriesWithPagination(String kw, Pageable pageable) {

        if (kw == null || kw.trim().isEmpty()) {
            return categoryRepository.findAll(pageable);
        } else {
            return categoryRepository.findByLabelContains(kw, pageable);
        }
    }
}
