package com.promoweb.mercadona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoweb.mercadona.MercadonaApplication;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.service.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MercadonaApplication.class)
@AutoConfigureMockMvc
public class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PromotionService promotionService;

    @Test
    public void testDeletePromotion() throws Exception {
        Long promotionId = 1L;

        // Création d'un objet Promotion simulé à envoyer dans la requête POST.
        Promotion promotionToCreate = new Promotion(1L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 31), 10.0);

        // Simulation de la suppression d'une promotion qui n'est pas trouvée
        //doThrow(new EntityNotFoundException("Promotion not found")).when(promotionService).deletePromotion(promotionId);

        //Simulation de la suppression de la promotion en utilisant le service promotionService.
        mockMvc.perform(delete("/api/promotions/{id}", promotionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotionToCreate)))
                        .andExpect(status().isNoContent())
                        .andExpect(jsonPath("$").doesNotExist());

        //Vérification que le service a été appelé avec l'identifiant de promotion spécifié
        verify(promotionService, times(1)).deletePromotion(promotionId);
    }

    @Test
    public void testCreatePromotion() throws Exception {

        // Création d'un objet Promotion simulé à envoyer dans la requête POST.
        Promotion promotionToCreate = new Promotion(null, LocalDate.of(2023, 9, 1), LocalDate.of(2023, 9, 30), 20.0);

        // Simulation de la création de la promotion en utilisant le service promotionService.
        when(promotionService.createPromotion(any(Promotion.class))).thenReturn(promotionToCreate);

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotionToCreate)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.discountPercentage").value(20.0));

        // Vérification que le service a été appelé avec la promotion simulée.
        verify(promotionService, times(1)).createPromotion(any(Promotion.class));
    }


}
