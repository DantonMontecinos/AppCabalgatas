package com.cabalgatas.service;

import com.cabalgatas.entity.Reserva;
import com.cabalgatas.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @deprecated Reservas are now embedded directly in Cabalgata entity.
 * This service is kept for backward compatibility but is no longer actively used.
 */
@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorCabalgata(Long cabalgataId) {
        return reservaRepository.findByCabalgataId(cabalgataId);
    }

    @Transactional(readOnly = true)
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + id));
    }

    public void eliminar(Long id) {
        Reserva reserva = obtenerPorId(id);
        reservaRepository.delete(reserva);
    }
}
