package com.cabalgatas.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CabalgataDetalleDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String clienteNombre;
    private String clienteTelefono;
    private int cantidadPersonas;
    private List<CaballoDTO> caballos;
    private List<GuiaDTO> guias;
    private List<String> estadosPosibles;
    private String estadoPago;
    private int porcentajePagado;

    public static class CaballoDTO {
        private Long id;
        private String nombre;
        private String estado;

        public CaballoDTO() {}
        public CaballoDTO(Long id, String nombre, String estado) {
            this.id = id; this.nombre = nombre; this.estado = estado;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }

    public static class GuiaDTO {
        private Long id;
        private String nombre;
        private String telefono;

        public GuiaDTO() {}
        public GuiaDTO(Long id, String nombre, String telefono) {
            this.id = id; this.nombre = nombre; this.telefono = telefono;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }
    public int getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }
    public List<CaballoDTO> getCaballos() { return caballos; }
    public void setCaballos(List<CaballoDTO> caballos) { this.caballos = caballos; }
    public List<GuiaDTO> getGuias() { return guias; }
    public void setGuias(List<GuiaDTO> guias) { this.guias = guias; }
    public List<String> getEstadosPosibles() { return estadosPosibles; }
    public void setEstadosPosibles(List<String> estadosPosibles) { this.estadosPosibles = estadosPosibles; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public int getPorcentajePagado() { return porcentajePagado; }
    public void setPorcentajePagado(int porcentajePagado) { this.porcentajePagado = porcentajePagado; }
}
