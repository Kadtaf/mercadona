package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Read
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return  ResponseEntity.ok(category);
        } else {
            throw new EntityNotFoundException("La catégorie avec l'id : " + id + " n'existe pas");
        }
    }

    //Create
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category ) {
        Category createCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updateCategory = categoryService.updateCategory(id, category);
        if (updateCategory != null) {
            return ResponseEntity.ok(updateCategory);
        } else {
            throw new EntityNotFoundException("La catégorie avec l'id : " + id + " n'existe pas");
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    //Show categories
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
