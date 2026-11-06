package com.utch.pruebas_unitarias.service;

import com.utch.pruebas_unitarias.exception.ProductoNoEncontradoException;
import com.utch.pruebas_unitarias.model.Producto;
import com.utch.pruebas_unitarias.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    // ---------- guardar ----------
    @Test
    @DisplayName("guardar: persiste un producto válido")
    void guardar_productoValido_loPersiste() {
        Producto entrada = new Producto(null, "Laptop", 15000.0, 5);
        Producto guardado = new Producto(1L, "Laptop", 15000.0, 5);
        when(repository.save(any(Producto.class))).thenReturn(guardado);

        Producto resultado = service.guardar(entrada);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Laptop");
        verify(repository, times(1)).save(entrada);
    }

    @Test
    @DisplayName("guardar: envía al repositorio los datos correctos")
    void guardar_capturaArgumentoEnviado() {
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        service.guardar(new Producto(null, "Mouse", 250.0, 20));

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo("Mouse");
        assertThat(captor.getValue().getPrecio()).isEqualTo(250.0);
    }

    @Test
    @DisplayName("guardar: lanza excepción si el nombre está vacío")
    void guardar_nombreVacio_lanzaExcepcion() {
        Producto invalido = new Producto(null, "  ", 100.0, 1);

        assertThrows(IllegalArgumentException.class, () -> service.guardar(invalido));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("guardar: lanza excepción si el precio es menor o igual a 0")
    void guardar_precioInvalido_lanzaExcepcion() {
        Producto invalido = new Producto(null, "Teclado", 0.0, 1);

        assertThrows(IllegalArgumentException.class, () -> service.guardar(invalido));
        verify(repository, never()).save(any());
    }

    // ---------- obtenerPorId ----------
    @Test
    @DisplayName("obtenerPorId: devuelve el producto cuando existe")
    void obtenerPorId_existente_devuelveProducto() {
        Producto p = new Producto(1L, "Laptop", 15000.0, 5);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Producto resultado = service.obtenerPorId(1L);

        assertThat(resultado).isEqualTo(p);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId: lanza excepción cuando no existe")
    void obtenerPorId_inexistente_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    // ---------- listar / buscar ----------
    @Test
    @DisplayName("listarTodos: devuelve todos los productos")
    void listarTodos_devuelveLista() {
        when(repository.findAll()).thenReturn(List.of(
                new Producto(1L, "Laptop", 15000.0, 5),
                new Producto(2L, "Mouse", 250.0, 20)));

        List<Producto> resultado = service.listarTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Producto::getNombre).containsExactly("Laptop", "Mouse");
    }

    @Test
    @DisplayName("buscarPorNombre: devuelve coincidencias")
    void buscarPorNombre_devuelveCoincidencias() {
        when(repository.findByNombreContainingIgnoreCase("lap"))
                .thenReturn(List.of(new Producto(1L, "Laptop", 15000.0, 5)));

        List<Producto> resultado = service.buscarPorNombre("lap");

        assertThat(resultado).hasSize(1);
        verify(repository).findByNombreContainingIgnoreCase("lap");
    }

    // ---------- eliminar ----------
    @Test
    @DisplayName("eliminar: borra cuando el producto existe")
    void eliminar_existente_llamaDeleteById() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar: lanza excepción si no existe y no borra nada")
    void eliminar_inexistente_lanzaExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNoEncontradoException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}