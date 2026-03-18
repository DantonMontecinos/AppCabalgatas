package com.cabalgatas.controller;

import com.cabalgatas.entity.Caballo;
import com.cabalgatas.service.CaballoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class CaballoController {

    private final CaballoService caballoService;

    public CaballoController(CaballoService caballoService) {
        this.caballoService = caballoService;
    }

    // ========== Vistas Thymeleaf ==========

    @GetMapping("/caballos")
    public String listar(Model model) {
        model.addAttribute("caballos", caballoService.listarTodos());
        return "caballos";
    }

    // ========== API REST ==========

    @GetMapping("/api/caballos")
    @ResponseBody
    public List<Caballo> listarTodos() {
        return caballoService.listarTodos();
    }

    @GetMapping("/api/caballos/disponibles")
    @ResponseBody
    public List<Caballo> listarDisponibles() {
        return caballoService.listarDisponibles();
    }

    @PostMapping("/api/caballos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Caballo caballo) {
        Caballo nuevo = caballoService.crear(caballo);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Caballo creado exitosamente",
                "id", nuevo.getId()
        ));
    }

    @PutMapping("/api/caballos/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody Caballo caballo) {
        caballoService.actualizar(id, caballo);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Caballo actualizado exitosamente"
        ));
    }

    @DeleteMapping("/api/caballos/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        caballoService.eliminar(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Caballo eliminado exitosamente"
        ));
    }
}
