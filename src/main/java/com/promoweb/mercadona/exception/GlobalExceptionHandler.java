package com.promoweb.mercadona.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(basePackages = "com.promoweb.mercadona")
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "errors/error";
    }

    @ExceptionHandler(NoProductsFoundException.class)
    public String handleNoProductsFoundException(NoProductsFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "errors/error";
    }

}
