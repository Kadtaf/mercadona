package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.repository.CategoryRepository;
import com.promoweb.mercadona.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


import javax.transaction.Transactional;
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

            return categoryRepository.save(category);

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


    public Page<Category> findCategoriesWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {
            // Si le mot-clé est vide, récupérez tous les utilisateurs avec pagination
            return categoryRepository.findAll(pageable);
        } else {
            // Si un mot-clé est fourni, recherchez les utilisateurs par mot-clé avec pagination
            return categoryRepository.findByLabelContains(kw, pageable);
        }
    }
}
