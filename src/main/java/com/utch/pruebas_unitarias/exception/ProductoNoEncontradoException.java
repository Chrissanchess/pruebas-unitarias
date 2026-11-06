package com.utch.pruebas_unitarias.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(Long id) {
        super("Producto no encontrado con id: " + id);
    }
}