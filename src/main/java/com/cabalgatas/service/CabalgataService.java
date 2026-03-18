package com.cabalgatas.service;

import com.cabalgatas.dto.*;
import com.cabalgatas.entity.*;
import com.cabalgatas.exception.ValidacionException;
import com.cabalgatas.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CabalgataService {

    private final CabalgataRepository cabalgataRepository;
    private final CaballoRepository caballoRepository;
    private final GuiaRepository guiaRepository;

    public CabalgataService(CabalgataRepository cabalgataRepository,
                            CaballoRepository caballoRepository,
                            GuiaRepository guiaRepository) {
        this.cabalgataRepository = cabalgataRepository;
        this.caballoRepository = caballoRepository;
        this.guiaRepository = guiaRepository;
    }

    // ========== CRUD ==========

    @Transactional(readOnly = true)
    public List<Cabalgata> listarTodas() {
        return cabalgataRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cabalgata obtenerPorId(Long id) {
        return cabalgataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cabalgata no encontrada con id: " + id));
    }

    public Cabalgata crear(CabalgataDTO dto) {
        validarHorarios(dto.getHoraInicio(), dto.getHoraFin());

        Cabalgata cabalgata = new Cabalgata();
        cabalgata.setFecha(dto.getFecha());
        cabalgata.setHoraInicio(dto.getHoraInicio());
        cabalgata.setHoraFin(dto.getHoraFin());
        cabalgata.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoCabalgata.BORRADOR);
        cabalgata.setClienteNombre(dto.getClienteNombre());
        cabalgata.setClienteTelefono(dto.getClienteTelefono());
        cabalgata.setCantidadPersonas(dto.getCantidadPersonas());

        return cabalgataRepository.save(cabalgata);
    }

    public Cabalgata actualizar(Long id, CabalgataDTO dto) {
        Cabalgata cabalgata = obtenerPorId(id);
        validarHorarios(dto.getHoraInicio(), dto.getHoraFin());

        boolean horarioCambio = !cabalgata.getFecha().equals(dto.getFecha())
                || !cabalgata.getHoraInicio().equals(dto.getHoraInicio())
                || !cabalgata.getHoraFin().equals(dto.getHoraFin());

        cabalgata.setFecha(dto.getFecha());
        cabalgata.setHoraInicio(dto.getHoraInicio());
        cabalgata.setHoraFin(dto.getHoraFin());
        cabalgata.setClienteNombre(dto.getClienteNombre());
        cabalgata.setClienteTelefono(dto.getClienteTelefono());
        cabalgata.setCantidadPersonas(dto.getCantidadPersonas());

        if (dto.getEstado() != null) {
            cabalgata.setEstado(dto.getEstado());
        }

        if (horarioCambio) {
            for (Caballo caballo : cabalgata.getCaballos()) {
                validarSolapamientoCaballo(cabalgata, caballo);
            }
            for (Guia guia : cabalgata.getGuias()) {
                validarSolapamientoGuia(cabalgata, guia);
            }
        }

        return cabalgataRepository.save(cabalgata);
    }

    public void eliminar(Long id) {
        Cabalgata cabalgata = obtenerPorId(id);

        for (Caballo caballo : cabalgata.getCaballos()) {
            caballo.setDisponible(true);
            caballoRepository.save(caballo);
        }
        for (Guia guia : cabalgata.getGuias()) {
            guia.setActivo(true);
            guiaRepository.save(guia);
        }

        cabalgata.getCaballos().clear();
        cabalgata.getGuias().clear();
        cabalgataRepository.save(cabalgata);
        cabalgataRepository.delete(cabalgata);
    }

    // ========== State Transition ==========

    public Cabalgata cambiarEstado(Long id, EstadoCabalgata nuevoEstado) {
        Cabalgata cabalgata = obtenerPorId(id);
        EstadoCabalgata estadoActual = cabalgata.getEstado();

        List<EstadoCabalgata> permitidos = getEstadosPosibles(estadoActual);
        if (!permitidos.contains(nuevoEstado)) {
            throw new ValidacionException("No se puede cambiar de " + estadoActual + " a " + nuevoEstado);
        }

        cabalgata.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoCabalgata.FINALIZADA || nuevoEstado == EstadoCabalgata.CANCELADA) {
            for (Caballo caballo : cabalgata.getCaballos()) {
                caballo.setDisponible(true);
                caballoRepository.save(caballo);
            }
            for (Guia guia : cabalgata.getGuias()) {
                guia.setActivo(true);
                guiaRepository.save(guia);
            }
        }

        return cabalgataRepository.save(cabalgata);
    }

    public List<EstadoCabalgata> getEstadosPosibles(EstadoCabalgata estadoActual) {
        List<EstadoCabalgata> posibles = new ArrayList<>();
        switch (estadoActual) {
            case BORRADOR -> {
                posibles.add(EstadoCabalgata.PROGRAMADA);
                posibles.add(EstadoCabalgata.CANCELADA);
            }
            case PROGRAMADA -> {
                posibles.add(EstadoCabalgata.EN_CURSO);
                posibles.add(EstadoCabalgata.CANCELADA);
            }
            case EN_CURSO -> {
                posibles.add(EstadoCabalgata.FINALIZADA);
                posibles.add(EstadoCabalgata.CANCELADA);
            }
            case FINALIZADA, CANCELADA -> { }
        }
        return posibles;
    }

    // ========== Asignación de Caballos ==========

    public Cabalgata asignarCaballo(Long cabalgataId, Long caballoId) {
        Cabalgata cabalgata = obtenerPorId(cabalgataId);
        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new EntityNotFoundException("Caballo no encontrado con id: " + caballoId));

        if (!caballo.isDisponible()) {
            throw new ValidacionException("El caballo '" + caballo.getNombre() + "' no está disponible");
        }
        if (cabalgata.getCaballos().contains(caballo)) {
            throw new ValidacionException("El caballo '" + caballo.getNombre() + "' ya está asignado");
        }

        cabalgata.getCaballos().add(caballo);
        caballo.setDisponible(false);
        caballoRepository.save(caballo);
        return cabalgataRepository.save(cabalgata);
    }

    public Cabalgata removerCaballo(Long cabalgataId, Long caballoId) {
        Cabalgata cabalgata = obtenerPorId(cabalgataId);
        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new EntityNotFoundException("Caballo no encontrado"));

        cabalgata.getCaballos().remove(caballo);
        caballo.setDisponible(true);
        caballoRepository.save(caballo);
        return cabalgataRepository.save(cabalgata);
    }

    // ========== Asignación de Guías ==========

    public Cabalgata asignarGuia(Long cabalgataId, Long guiaId) {
        Cabalgata cabalgata = obtenerPorId(cabalgataId);
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new EntityNotFoundException("Guía no encontrado con id: " + guiaId));

        if (!guia.isActivo()) {
            throw new ValidacionException("El guía '" + guia.getNombre() + "' no está activo");
        }
        if (cabalgata.getGuias().contains(guia)) {
            throw new ValidacionException("El guía '" + guia.getNombre() + "' ya está asignado");
        }

        cabalgata.getGuias().add(guia);
        guia.setActivo(false);
        guiaRepository.save(guia);
        return cabalgataRepository.save(cabalgata);
    }

    public Cabalgata removerGuia(Long cabalgataId, Long guiaId) {
        Cabalgata cabalgata = obtenerPorId(cabalgataId);
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new EntityNotFoundException("Guía no encontrado"));

        cabalgata.getGuias().remove(guia);
        guia.setActivo(true);
        guiaRepository.save(guia);
        return cabalgataRepository.save(cabalgata);
    }

    // ========== Detalle DTO ==========

    @Transactional(readOnly = true)
    public CabalgataDetalleDTO obtenerDetalle(Long id) {
        Cabalgata c = obtenerPorId(id);
        CabalgataDetalleDTO dto = new CabalgataDetalleDTO();
        dto.setId(c.getId());
        dto.setFecha(c.getFecha());
        dto.setHoraInicio(c.getHoraInicio());
        dto.setHoraFin(c.getHoraFin());
        dto.setEstado(c.getEstado().name());
        dto.setClienteNombre(c.getClienteNombre());
        dto.setClienteTelefono(c.getClienteTelefono());
        dto.setCantidadPersonas(c.getTotalPersonas());

        dto.setEstadosPosibles(
            getEstadosPosibles(c.getEstado()).stream()
                .map(Enum::name)
                .collect(Collectors.toList())
        );

        dto.setCaballos(c.getCaballos().stream()
                .map(cab -> new CabalgataDetalleDTO.CaballoDTO(cab.getId(), cab.getNombre(), cab.getEstado()))
                .collect(Collectors.toList()));

        dto.setGuias(c.getGuias().stream()
                .map(g -> new CabalgataDetalleDTO.GuiaDTO(g.getId(), g.getNombre(), g.getTelefono()))
                .collect(Collectors.toList()));

        return dto;
    }

    // ========== Calendar Events ==========

    @Transactional(readOnly = true)
    public List<CalendarEventDTO> obtenerEventosCalendario(LocalDate inicio, LocalDate fin) {
        List<Cabalgata> cabalgatas = cabalgataRepository.findByFechaBetween(inicio, fin);
        return cabalgatas.stream().map(this::toCalendarEvent).collect(Collectors.toList());
    }

    private CalendarEventDTO toCalendarEvent(Cabalgata c) {
        String title;
        if (c.getClienteNombre() != null && !c.getClienteNombre().isBlank()) {
            title = c.getClienteNombre() + " (" + c.getTotalPersonas() + " pers.)";
        } else {
            title = "Cabalgata — Sin reserva";
        }

        String start = LocalDateTime.of(c.getFecha(), c.getHoraInicio())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String end = LocalDateTime.of(c.getFecha(), c.getHoraFin())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String color = getColorPorEstado(c.getEstado());

        return new CalendarEventDTO(c.getId(), title, start, end, color);
    }

    private String getColorPorEstado(EstadoCabalgata estado) {
        return switch (estado) {
            case BORRADOR -> "#6b7280";
            case PROGRAMADA -> "#28a745";
            case EN_CURSO -> "#007bff";
            case FINALIZADA -> "#6c757d";
            case CANCELADA -> "#dc3545";
        };
    }

    // ========== Validaciones ==========

    private void validarHorarios(java.time.LocalTime horaInicio, java.time.LocalTime horaFin) {
        if (horaInicio == null || horaFin == null) return;
        if (!horaFin.isAfter(horaInicio)) {
            throw new ValidacionException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private void validarSolapamientoCaballo(Cabalgata cabalgata, Caballo caballo) {
        List<Cabalgata> solapadas = cabalgataRepository.findSolapadas(
                cabalgata.getFecha(), cabalgata.getHoraInicio(), cabalgata.getHoraFin(), cabalgata.getId());
        for (Cabalgata otra : solapadas) {
            if (otra.getCaballos().contains(caballo)) {
                throw new ValidacionException("El caballo '" + caballo.getNombre() + "' ya está asignado a otra cabalgata solapada");
            }
        }
    }

    private void validarSolapamientoGuia(Cabalgata cabalgata, Guia guia) {
        List<Cabalgata> solapadas = cabalgataRepository.findSolapadas(
                cabalgata.getFecha(), cabalgata.getHoraInicio(), cabalgata.getHoraFin(), cabalgata.getId());
        for (Cabalgata otra : solapadas) {
            if (otra.getGuias().contains(guia)) {
                throw new ValidacionException("El guía '" + guia.getNombre() + "' ya está asignado a otra cabalgata solapada");
            }
        }
    }
}
