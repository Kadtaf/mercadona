package com.promoweb.mercadona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.service.CategoryService;
import com.promoweb.mercadona.service.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Mock
    private CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testShowUpdateForm() throws Exception {
        // Setup MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        // Create a sample product for testing
        Long productId = 1L;

        // Mock productService.getProductById to return a product
        when(productService.getProductById(productId)).thenReturn(new Product());

        // Perform the request
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/editProduct/{id}", productId));

        // Validate the response
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("products/editProduct"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("categories"));

        // Verify that productService.getProductById was called
        verify(productService, times(1)).getProductById(productId);
    }

}
