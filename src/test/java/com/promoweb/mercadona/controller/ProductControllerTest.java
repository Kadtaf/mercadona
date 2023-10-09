package com.promoweb.mercadona.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoweb.mercadona.MercadonaApplication;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@RunWith(SpringRunner.class)
@SpringBootTest(classes = MercadonaApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    public void testCreateProduct() throws Exception {
        //Création d'un objet Product simulé à envoyer dans la requête POST.
        Product productToCreate = new Product("NewProduct", "NewProduct", 19.99, "newProduct.jpg", null, null);

        //Simulation de la création du produit en utilisant le service productService.
        when(productService.createProduct(any(Product.class))).thenReturn(productToCreate);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.label").value("NewProduct"));

        //Verification que le service a été appelé avec le produit simulé
        verify(productService, times(1)).createProduct(any(Product.class));
    }
}
