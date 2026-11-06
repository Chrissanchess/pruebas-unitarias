package com.utch.pruebas_unitarias.service;

import com.utch.pruebas_unitarias.exception.ProductoNoEncontradoException;
import com.utch.pruebas_unitarias.model.Producto;
import com.utch.pruebas_unitarias.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public Producto guardar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        return repository.save(producto);
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        repository.deleteById(id);
    }
}