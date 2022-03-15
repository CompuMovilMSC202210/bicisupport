package com.javeriana.bicisupport.models;

import java.io.Serializable;

public class Incident implements Serializable {
    int numero;
    String novedad;
    String direccion;
    String fecha;
    String detalle;
    String empresa;
    String serviciosp;
    Double costos;

    public Incident(int numero, String novedad, String direccion, String fecha, String detalle, String empresa, String serviciosp, Double costos) {
        this.numero = numero;
        this.novedad = novedad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.detalle = detalle;
        this.empresa = empresa;
        this.serviciosp = serviciosp;
        this.costos = costos;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getServiciosp() {
        return serviciosp;
    }

    public void setServiciosp(String serviciosp) {
        this.serviciosp = serviciosp;
    }

    public Double getCostos() {
        return costos;
    }

    public void setCostos(Double costos) {
        this.costos = costos;
    }
}
