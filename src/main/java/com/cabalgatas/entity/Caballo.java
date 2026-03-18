package com.cabalgatas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "caballos")
public class Caballo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del caballo es obligatorio")
    private String nombre;

    private String estado;

    private boolean disponible = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "caballos")
    private Set<Cabalgata> cabalgatas = new HashSet<>();

    // === Constructors ===
    public Caballo() {}

    public Caballo(String nombre, String estado, boolean disponible) {
        this.nombre = nombre;
        this.estado = estado;
        this.disponible = disponible;
    }

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public Set<Cabalgata> getCabalgatas() { return cabalgatas; }
    public void setCabalgatas(Set<Cabalgata> cabalgatas) { this.cabalgatas = cabalgatas; }
}
