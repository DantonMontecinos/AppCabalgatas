package com.cabalgatas.service;

import com.cabalgatas.entity.Caballo;
import com.cabalgatas.repository.CaballoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CaballoService {

    private final CaballoRepository caballoRepository;

    public CaballoService(CaballoRepository caballoRepository) {
        this.caballoRepository = caballoRepository;
    }

    @Transactional(readOnly = true)
    public List<Caballo> listarTodos() {
        return caballoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Caballo> listarDisponibles() {
        return caballoRepository.findByDisponibleTrue();
    }

    @Transactional(readOnly = true)
    public Caballo obtenerPorId(Long id) {
        return caballoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caballo no encontrado con id: " + id));
    }

    public Caballo crear(Caballo caballo) {
        return caballoRepository.save(caballo);
    }

    public Caballo actualizar(Long id, Caballo datos) {
        Caballo caballo = obtenerPorId(id);
        caballo.setNombre(datos.getNombre());
        caballo.setEstado(datos.getEstado());
        caballo.setDisponible(datos.isDisponible());
        return caballoRepository.save(caballo);
    }

    public void eliminar(Long id) {
        Caballo caballo = obtenerPorId(id);
        caballoRepository.delete(caballo);
    }
}
