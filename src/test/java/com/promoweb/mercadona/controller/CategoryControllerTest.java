package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.service.CategoryService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void testIndex() {
        // Mocking
        when(categoryService.findCategoriesWithPagination(any(), any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(Collections.emptyList()));

        // Test
        String viewName = categoryController.index(model, 0, 4, "");

        // Verify
        assertEquals("/categories/listCategories", viewName);
    }

    @Test
    void testGetCategoryById() {
        // Mocking
        when(categoryService.getCategoryById(Mockito.anyLong()))
                .thenReturn(new Category());

        // Test
        String viewName = categoryController.getCategoryById(1L, model);

        // Verify
        assertEquals("/categories/editCategory", viewName);
    }

    @Test
    void testUpdateCategory() {
        // Mocking
        when(bindingResult.hasErrors()).thenReturn(false);

        // Test
        String viewName = categoryController.updateCategory(1L, new Category(), bindingResult, redirectAttributes, model);

        // Verify
        assertEquals("redirect:../index", viewName);
    }

    @Test
    void testCreateCategory() {
        // Mocking
        when(bindingResult.hasErrors()).thenReturn(false);

        // Test
        String viewName = categoryController.createCategory(new Category(), bindingResult, redirectAttributes, model);

        // Verify
        assertEquals("redirect:index", viewName);
    }

    @Test
    void testDeleteCategory() {
        // Mocking
        when(categoryService.getCategoryById(Mockito.anyLong()))
                .thenReturn(new Category());

        // Test
        String viewName = categoryController.deleteCategory(1L, redirectAttributes);

        // Verify
        assertEquals("redirect:../index", viewName);
    }


}
