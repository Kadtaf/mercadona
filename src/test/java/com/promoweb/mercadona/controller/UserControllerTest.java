package com.promoweb.mercadona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.UserService;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.mockito.Mock;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes mockRedirectAttributes;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateUser() {

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(any())).thenReturn(new User());

        String viewName = userController.createUser(new User(), bindingResult, mockRedirectAttributes, model);

        assertEquals("redirect:index", viewName);
        verify(model).addAttribute(eq("createdUser"), any(User.class));
    }

    @Test
    void testIndex() {

        Page<User> mockPage = mock(Page.class);
        when(userService.findUsersWithPagination(anyString(), any())).thenReturn(mockPage);

        String viewName = userController.index(model, 0, 4, "");

        assertEquals("/users/listUser", viewName);
        verify(model).addAttribute(eq("users"), anyList());
        verify(model).addAttribute(eq("pages"), any(int[].class));
        verify(model).addAttribute(eq("currentPage"), eq(0));
        verify(model).addAttribute(eq("keyword"), eq(""));
    }

    @Test
    void testCreateUserWithValidationErrors() {

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.createUser(new User(), bindingResult, mockRedirectAttributes, model);

        assertEquals("/users/formUser", viewName);
        verifyNoInteractions(userService); // Aucun appel à userService.createUser lorsqu'il y a des erreurs de validation
        verify(model, never()).addAttribute(eq("createdUser"), any(User.class)); // Aucun ajout au modèle en cas d'erreurs de validation
    }

}
