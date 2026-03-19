package com.cabalgatas.controller;

import com.cabalgatas.dto.*;
import com.cabalgatas.entity.*;
import com.cabalgatas.service.CabalgataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Controller
public class CabalgataController {

    private final CabalgataService cabalgataService;

    public CabalgataController(CabalgataService cabalgataService) {
        this.cabalgataService = cabalgataService;
    }

    // ===== Thymeleaf View =====
    @GetMapping("/")
    public String calendar() {
        return "calendar";
    }

    // ===== REST API =====

    /**
     * FIX: FullCalendar sends dates as ISO-8601 datetime strings with timezone
     * (e.g. "2026-03-01T00:00:00-03:00"), NOT plain dates ("2026-03-01").
     * We accept String and parse flexibly.
     */
    @GetMapping("/api/cabalgatas")
    @ResponseBody
    public List<CalendarEventDTO> getCalendarEvents(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDate startDate = parseFlexibleDate(start);
        LocalDate endDate = parseFlexibleDate(end);
        return cabalgataService.obtenerEventosCalendario(startDate, endDate);
    }

    /**
     * Parse a date string that could be:
     * - "2026-03-01" (plain date)
     * - "2026-03-01T00:00:00" (datetime without timezone)
     * - "2026-03-01T00:00:00-03:00" (datetime with timezone offset)
     */
    private LocalDate parseFlexibleDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDate.now();
        }
        // Try plain date first
        try {
            return LocalDate.parse(dateStr.substring(0, 10));
        } catch (DateTimeParseException e) {
            // Fallback: try full ISO offset datetime
            try {
                return OffsetDateTime.parse(dateStr).toLocalDate();
            } catch (DateTimeParseException e2) {
                return LocalDate.now();
            }
        }
    }

    @GetMapping("/api/cabalgatas/{id}")
    @ResponseBody
    public CabalgataDetalleDTO getDetalle(@PathVariable Long id) {
        return cabalgataService.obtenerDetalle(id);
    }

    /**
     * FIX: Return Map instead of Cabalgata entity to avoid
     * lazy-loading serialization issues with caballos/guias sets.
     */
    @PostMapping("/api/cabalgatas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crear(@RequestBody CabalgataDTO dto) {
        Cabalgata c = cabalgataService.crear(dto);
        return ResponseEntity.ok(Map.of("message", "Cabalgata creada", "id", c.getId()));
    }

    @PutMapping("/api/cabalgatas/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody CabalgataDTO dto) {
        Cabalgata c = cabalgataService.actualizar(id, dto);
        return ResponseEntity.ok(Map.of("message", "Cabalgata actualizada", "id", c.getId()));
    }

    @DeleteMapping("/api/cabalgatas/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        try {
            cabalgataService.eliminar(id);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            // Ya estaba eliminada, se considera exitoso para hacer el endpoint idempotente
            return ResponseEntity.ok(Map.of("message", "Cabalgata eliminada correctamente"));
        }
        return ResponseEntity.ok(Map.of("message", "Cabalgata eliminada correctamente"));
    }

    // ===== State Transition =====
    @PutMapping("/api/cabalgatas/{id}/estado")
    @ResponseBody
    public ResponseEntity<Map<String, String>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        EstadoCabalgata nuevoEstado = EstadoCabalgata.valueOf(body.get("estado"));
        cabalgataService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(Map.of("message", "Estado cambiado a " + nuevoEstado.name()));
    }

    // ===== Horse Assignment =====
    @PostMapping("/api/cabalgatas/{id}/asignar-caballo/{caballoId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> asignarCaballo(@PathVariable Long id, @PathVariable Long caballoId) {
        cabalgataService.asignarCaballo(id, caballoId);
        return ResponseEntity.ok(Map.of("message", "Caballo asignado correctamente"));
    }

    @DeleteMapping("/api/cabalgatas/{id}/remover-caballo/{caballoId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> removerCaballo(@PathVariable Long id, @PathVariable Long caballoId) {
        cabalgataService.removerCaballo(id, caballoId);
        return ResponseEntity.ok(Map.of("message", "Caballo removido correctamente"));
    }

    // ===== Guide Assignment =====
    @PostMapping("/api/cabalgatas/{id}/asignar-guia/{guiaId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> asignarGuia(@PathVariable Long id, @PathVariable Long guiaId) {
        cabalgataService.asignarGuia(id, guiaId);
        return ResponseEntity.ok(Map.of("message", "Guía asignado correctamente"));
    }

    @DeleteMapping("/api/cabalgatas/{id}/remover-guia/{guiaId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> removerGuia(@PathVariable Long id, @PathVariable Long guiaId) {
        cabalgataService.removerGuia(id, guiaId);
        return ResponseEntity.ok(Map.of("message", "Guía removido correctamente"));
    }

    // ===== Payment Status =====
    @PutMapping("/api/cabalgatas/{id}/estado-pago")
    @ResponseBody
    public ResponseEntity<Map<String, String>> actualizarPago(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        int porcentaje = body.get("porcentajePagado") != null
                ? ((Number) body.get("porcentajePagado")).intValue() : 0;
        cabalgataService.actualizarPago(id, porcentaje);
        return ResponseEntity.ok(Map.of("message", "Estado de pago actualizado"));
    }

    // ===== En Curso List =====
    @GetMapping("/api/cabalgatas/en-curso")
    @ResponseBody
    public List<Map<String, Object>> listarEnCurso() {
        return cabalgataService.listarEnCurso();
    }
}
