package com.utch.pruebas_unitarias.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoTest {

    @Test
    @DisplayName("constructor con argumentos asigna todos los campos")
    void constructor_asignaCampos() {
        Producto p = new Producto(1L, "Laptop", 15000.0, 5);

        assertThat(p.getId()).isEqualTo(1L);
        assertThat(p.getNombre()).isEqualTo("Laptop");
        assertThat(p.getPrecio()).isEqualTo(15000.0);
        assertThat(p.getStock()).isEqualTo(5);
    }

    @Test
    @DisplayName("setters actualizan los valores del producto")
    void setters_actualizanValores() {
        Producto p = new Producto();

        p.setId(2L);
        p.setNombre("Mouse");
        p.setPrecio(250.0);
        p.setStock(20);

        assertThat(p.getId()).isEqualTo(2L);
        assertThat(p.getNombre()).isEqualTo("Mouse");
        assertThat(p.getPrecio()).isEqualTo(250.0);
        assertThat(p.getStock()).isEqualTo(20);
    }
}