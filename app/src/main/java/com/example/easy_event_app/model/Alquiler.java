package com.example.easy_event_app.model;

import java.util.Date;

public class Alquiler {

    private long id;
    private long user_id;

    private String nombre;
    private String apellido;
    private String metodo_pago;
    private String lugar_entrega;
    private String fecha_alquiler;
    private String fecha_devolucion;
    private String estado_pedido;
    private String estado_secuencia;
    private long precio_envio;
    private long costos_adicionales;
    private long precio_alquiler;


    public Alquiler(long id, long user_id, String nombre, String apellido, String metodo_pago, String lugar_entrega, String fecha_alquiler, String fecha_devolucion, String estado_pedido, String estado_secuencia, long precio_envio, long costos_adicionales, long precio_alquiler) {
        this.id = id;
        this.user_id = user_id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.metodo_pago = metodo_pago;
        this.lugar_entrega = lugar_entrega;
        this.fecha_alquiler = fecha_alquiler;
        this.fecha_devolucion = fecha_devolucion;
        this.estado_pedido = estado_pedido;
        this.estado_secuencia = estado_secuencia;
        this.precio_envio = precio_envio;
        this.costos_adicionales = costos_adicionales;
        this.precio_alquiler = precio_alquiler;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getLugar_entrega() {
        return lugar_entrega;
    }

    public void setLugar_entrega(String lugar_entrega) {
        this.lugar_entrega = lugar_entrega;
    }

    public String getFecha_alquiler() {
        return fecha_alquiler;
    }

    public void setFecha_alquiler(String fecha_alquiler) {
        this.fecha_alquiler = fecha_alquiler;
    }

    public String getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(String fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public String getEstado_pedido() {
        return estado_pedido;
    }

    public void setEstado_pedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }

    public String getEstado_secuencia() {
        return estado_secuencia;
    }

    public void setEstado_secuencia(String estado_secuencia) {
        this.estado_secuencia = estado_secuencia;
    }

    public long getPrecio_envio() {
        return precio_envio;
    }

    public void setPrecio_envio(long precio_envio) {
        this.precio_envio = precio_envio;
    }

    public long getCostos_adicionales() {
        return costos_adicionales;
    }

    public void setCostos_adicionales(long costos_adicionales) {
        this.costos_adicionales = costos_adicionales;
    }

    public long getPrecio_alquiler() {
        return precio_alquiler;
    }

    public void setPrecio_alquiler(long precio_alquiler) {
        this.precio_alquiler = precio_alquiler;
    }


    @Override
    public String toString() {
        return "Alquiler{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", metodo_pago='" + metodo_pago + '\'' +
                ", lugar_entrega='" + lugar_entrega + '\'' +
                ", fecha_alquiler='" + fecha_alquiler + '\'' +
                ", fecha_devolucion='" + fecha_devolucion + '\'' +
                ", estado_pedido='" + estado_pedido + '\'' +
                ", estado_secuencia='" + estado_secuencia + '\'' +
                ", precio_envio=" + precio_envio +
                ", costos_adicionales=" + costos_adicionales +
                ", precio_alquiler=" + precio_alquiler +
                '}';
    }
}
