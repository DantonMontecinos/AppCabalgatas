package com.cabalgatas.dto;

import com.cabalgatas.entity.EstadoCabalgata;
import java.time.LocalDate;
import java.time.LocalTime;

public class CabalgataDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoCabalgata estado;
    private String clienteNombre;
    private String clienteTelefono;
    private Integer cantidadPersonas;
    private Integer porcentajePagado;

    public CabalgataDTO() {}

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

    public Integer getPorcentajePagado() { return porcentajePagado; }
    public void setPorcentajePagado(Integer porcentajePagado) { this.porcentajePagado = porcentajePagado; }
}
