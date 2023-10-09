package com.promoweb.mercadona.exception;

public class NoProductsFoundException extends RuntimeException {

    public NoProductsFoundException(String message) {
        super(message);
    }
}
