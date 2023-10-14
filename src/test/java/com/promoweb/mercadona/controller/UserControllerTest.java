package com.promoweb.mercadona.controller;

import com.promoweb.mercadona.MercadonaApplication;
import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.UserService;

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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        Long userId = 1L;
            User mockUser = new User(userId, "adminUsername", "adminPassword", "ADMIN", new ArrayList<>());

        when(userService.getUserById(userId)).thenReturn(mockUser);

        // Utilisez get pour construire la requête
        MockHttpServletRequestBuilder requestBuilder = get("/api/users/{id}", userId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("adminUsername")))
                .andExpect(jsonPath("$.password", is("adminPassword")));

            verify(userService, times(1)).getUserById(userId);
    }
}
