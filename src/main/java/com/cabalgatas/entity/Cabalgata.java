package com.cabalgatas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cabalgatas")
public class Cabalgata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoCabalgata estado = EstadoCabalgata.BORRADOR;

    // === Client Info (1 cabalgata = 1 client/group) ===
    @Column(name = "cliente_nombre")
    private String clienteNombre;

    @Column(name = "cliente_telefono")
    private String clienteTelefono;

    @Column(name = "cantidad_personas")
    private Integer cantidadPersonas;

    @ManyToMany
    @JoinTable(
        name = "cabalgata_caballo",
        joinColumns = @JoinColumn(name = "cabalgata_id"),
        inverseJoinColumns = @JoinColumn(name = "caballo_id")
    )
    private Set<Caballo> caballos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "cabalgata_guia",
        joinColumns = @JoinColumn(name = "cabalgata_id"),
        inverseJoinColumns = @JoinColumn(name = "guia_id")
    )
    private Set<Guia> guias = new HashSet<>();

    // === Constructors ===
    public Cabalgata() {}

    // === Helper Methods ===
    public int getTotalPersonas() {
        return cantidadPersonas != null ? cantidadPersonas : 0;
    }

    public boolean tieneSolapamiento(Cabalgata otra) {
        if (!this.fecha.equals(otra.getFecha())) return false;
        return this.horaInicio.isBefore(otra.getHoraFin())
                && this.horaFin.isAfter(otra.getHoraInicio());
    }

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public EstadoCabalgata getEstado() { return estado; }
    public void setEstado(EstadoCabalgata estado) { this.estado = estado; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

    public Integer getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(Integer cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }

    public Set<Caballo> getCaballos() { return caballos; }
    public void setCaballos(Set<Caballo> caballos) { this.caballos = caballos; }

    public Set<Guia> getGuias() { return guias; }
    public void setGuias(Set<Guia> guias) { this.guias = guias; }
}
