package com.cabalgatas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Column(name = "cliente_nombre")
    private String clienteNombre;

    @Column(name = "cliente_telefono")
    private String clienteTelefono;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 persona")
    @Column(name = "cantidad_personas")
    private Integer cantidadPersonas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cabalgata_id", nullable = false)
    private Cabalgata cabalgata;

    // === Constructors ===
    public Reserva() {}

    public Reserva(String clienteNombre, String clienteTelefono, Integer cantidadPersonas, Cabalgata cabalgata) {
        this.clienteNombre = clienteNombre;
        this.clienteTelefono = clienteTelefono;
        this.cantidadPersonas = cantidadPersonas;
        this.cabalgata = cabalgata;
    }

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

    public Integer getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(Integer cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }

    public Cabalgata getCabalgata() { return cabalgata; }
    public void setCabalgata(Cabalgata cabalgata) { this.cabalgata = cabalgata; }
}
