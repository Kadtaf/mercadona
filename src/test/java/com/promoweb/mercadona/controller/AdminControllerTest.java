package com.promoweb.mercadona.controller;

import com.promoweb.mercadona.MercadonaApplication;
import com.promoweb.mercadona.model.Admin;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MercadonaApplication.class)
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    public void testGetAdminById() throws Exception {
        Long adminId = 1L;
        Admin mockAdmin = new Admin(adminId, "adminUsername", "adminPassword", new ArrayList<>());

        when(adminService.getAdminById(adminId)).thenReturn(mockAdmin);

        // Utilisez get pour construire la requête
        MockHttpServletRequestBuilder requestBuilder = get("/api/admins/{id}", adminId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("adminUsername")))
                .andExpect(jsonPath("$.password", is("adminPassword")));

        verify(adminService, times(1)).getAdminById(adminId);
    }
}
