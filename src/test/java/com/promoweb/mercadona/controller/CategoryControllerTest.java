package com.promoweb.mercadona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoweb.mercadona.MercadonaApplication;
import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MercadonaApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateCategory() throws Exception {

        Long categoryId = 1L;

        //Création d'un objet Category simulé à envoyer dans la requête PUT.
        Category updatedCategory = new Category(categoryId, "UpdatedCategory");

        //Simulation de la mise à jour de la categorie en utilisant le service categoryService.
        when(categoryService.updateCategory(eq(categoryId), any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("UpdatedCategory"));

        //Vérification que le service a été appelé avec la categorie simulé.
        verify(categoryService, times(1)).updateCategory(eq(categoryId), any(Category.class));

    }
}
