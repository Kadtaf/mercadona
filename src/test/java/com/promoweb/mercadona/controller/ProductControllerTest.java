package com.promoweb.mercadona.controller;

import com.promoweb.mercadona.exception.NoProductsFoundException;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private MultipartFile imageFile;

    @InjectMocks
    private ProductController productController;

    @Test
    public void testListAllProducts() {
        // Mocking
        Mockito.when(productService.findProduct(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(Collections.emptyList()));

        // Test
        String viewName = productController.listAllProducts(model, 0, 4, 1L);

        // Verify
        assertEquals("products/produtcsFragment", viewName);
    }

    @Test
    public void testListAllProductsException() {
        // Mocking
        Mockito.when(productService.findProduct(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NoProductsFoundException("No products found"));

        // Test
        String viewName = productController.listAllProducts(model, 0, 5, 1L);

        // Verify
        assertEquals("products/produtcsFragment", viewName);
    }

    @Test
    public void testGetPromotionalProducts() {
        // Mocking
        Mockito.when(productService.getPromotionalProducts())
                .thenReturn(Collections.singletonList(new Product()));

        // Test
        ResponseEntity<List<Product>> responseEntity = productController.getPromotionalProducts();

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

}
