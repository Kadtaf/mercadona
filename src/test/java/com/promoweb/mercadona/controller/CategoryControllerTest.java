package com.promoweb.mercadona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testUpdateCategory() throws Exception {
        // Setup MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        // Create a sample category for testing
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        // Set other properties...

        // Mock categoryService.updateCategory to do nothing
        doNothing().when(categoryService).updateCategory(category);

        // Perform the request
        ResultActions resultActions = mockMvc.perform(post("/api/categories/editCategory/{id}", categoryId)
                .flashAttr("category", category));

        // Validate the response
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("../index"))
                .andExpect(flash().attribute("message", "Mis à jour de la catégorie avec succès"));

        // Verify that the updateCategory method was called
        verify(categoryService, times(1)).updateCategory(category);
    }

    // Add more tests for other controller methods as needed...
}
