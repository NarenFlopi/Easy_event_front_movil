package com.example.easy_event_app.model;

import java.util.Date;

public class User {

    private long id;
    private Integer rol_id;
    private Long cedula;
    private String nombre;
    private String apellido;
    private String email;
    private Date fecha_nacimiento;
    private Long telefono;
    private String estado;
    private String foto;

    public User(long id, Integer rol_id, Long cedula, String nombre, String apellido, String email, Date fecha_nacimiento, Long telefono, String estado, String foto) {
        this.id = id;
        this.rol_id = rol_id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.estado = estado;
        this.foto = foto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getRol_id() {
        return rol_id;
    }

    public void setRol_id(Integer rol_id) {
        this.rol_id = rol_id;
    }

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", rol_id=" + rol_id +
                ", cedula=" + cedula +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fecha_nacimiento=" + fecha_nacimiento +
                ", telefono=" + telefono +
                ", estado='" + estado + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
