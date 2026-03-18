package com.cabalgatas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "guias")
public class Guia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del guía es obligatorio")
    private String nombre;

    private String telefono;

    private boolean activo = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "guias")
    private Set<Cabalgata> cabalgatas = new HashSet<>();

    // === Constructors ===
    public Guia() {}

    public Guia(String nombre, String telefono, boolean activo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.activo = activo;
    }

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Set<Cabalgata> getCabalgatas() { return cabalgatas; }
    public void setCabalgatas(Set<Cabalgata> cabalgatas) { this.cabalgatas = cabalgatas; }
}
