package com.carritocompras.exception;

public class NotFoundException extends RuntimeException {

    // Constructor que recibe un mensaje
    public NotFoundException(String message) {
        super(message);
    }

    // Constructor que recibe un mensaje y una causa
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
