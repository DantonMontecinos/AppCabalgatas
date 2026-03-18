package com.cabalgatas.service;

import com.cabalgatas.entity.Guia;
import com.cabalgatas.repository.GuiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuiaService {

    private final GuiaRepository guiaRepository;

    public GuiaService(GuiaRepository guiaRepository) {
        this.guiaRepository = guiaRepository;
    }

    @Transactional(readOnly = true)
    public List<Guia> listarTodos() {
        return guiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Guia> listarActivos() {
        return guiaRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Guia obtenerPorId(Long id) {
        return guiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guía no encontrado con id: " + id));
    }

    public Guia crear(Guia guia) {
        return guiaRepository.save(guia);
    }

    public Guia actualizar(Long id, Guia datos) {
        Guia guia = obtenerPorId(id);
        guia.setNombre(datos.getNombre());
        guia.setTelefono(datos.getTelefono());
        guia.setActivo(datos.isActivo());
        return guiaRepository.save(guia);
    }

    public void eliminar(Long id) {
        Guia guia = obtenerPorId(id);
        guiaRepository.delete(guia);
    }
}
