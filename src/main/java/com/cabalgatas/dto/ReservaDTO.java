package com.cabalgatas.dto;

public class ReservaDTO {
    private Long id;
    private String clienteNombre;
    private String clienteTelefono;
    private Integer cantidadPersonas;
    private Long cabalgataId;

    public ReservaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

    public Integer getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(Integer cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }

    public Long getCabalgataId() { return cabalgataId; }
    public void setCabalgataId(Long cabalgataId) { this.cabalgataId = cabalgataId; }
}
