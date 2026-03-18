package com.cabalgatas.controller;

import com.cabalgatas.entity.Guia;
import com.cabalgatas.service.GuiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class GuiaController {

    private final GuiaService guiaService;

    public GuiaController(GuiaService guiaService) {
        this.guiaService = guiaService;
    }

    // ========== Vistas Thymeleaf ==========

    @GetMapping("/guias")
    public String listar(Model model) {
        model.addAttribute("guias", guiaService.listarTodos());
        return "guias";
    }

    // ========== API REST ==========

    @GetMapping("/api/guias")
    @ResponseBody
    public List<Guia> listarTodos() {
        return guiaService.listarTodos();
    }

    @GetMapping("/api/guias/activos")
    @ResponseBody
    public List<Guia> listarActivos() {
        return guiaService.listarActivos();
    }

    @PostMapping("/api/guias")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Guia guia) {
        Guia nuevo = guiaService.crear(guia);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Guía creado exitosamente",
                "id", nuevo.getId()
        ));
    }

    @PutMapping("/api/guias/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody Guia guia) {
        guiaService.actualizar(id, guia);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Guía actualizado exitosamente"
        ));
    }

    @DeleteMapping("/api/guias/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        guiaService.eliminar(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Guía eliminado exitosamente"
        ));
    }
}
