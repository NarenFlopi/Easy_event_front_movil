package com.example.easy_event_app.model;

public class Empresa {
    private long id;
    private Long nit_empresa;
    private String direccion_empresa;
    private String nombre_empresa;
    private Long telefono_empresa;
    private String email_empresa;
    private String estado;
    private Long user_id;

    public Empresa(long id, Long nit_empresa, String direccion_empresa, String nombre_empresa, Long telefono_empresa, String email_empresa, String estado, Long user_id) {
        this.id = id;
        this.nit_empresa = nit_empresa;
        this.direccion_empresa = direccion_empresa;
        this.nombre_empresa = nombre_empresa;
        this.telefono_empresa = telefono_empresa;
        this.email_empresa = email_empresa;
        this.estado = estado;
        this.user_id = user_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getNit_empresa() {
        return nit_empresa;
    }

    public void setNit_empresa(Long nit_empresa) {
        this.nit_empresa = nit_empresa;
    }

    public String getDireccion_empresa() {
        return direccion_empresa;
    }

    public void setDireccion_empresa(String direccion_empresa) {
        this.direccion_empresa = direccion_empresa;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public Long getTelefono_empresa() {
        return telefono_empresa;
    }

    public void setTelefono_empresa(Long telefono_empresa) {
        this.telefono_empresa = telefono_empresa;
    }

    public String getEmail_empresa() {
        return email_empresa;
    }

    public void setEmail_empresa(String email_empresa) {
        this.email_empresa = email_empresa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nit_empresa=" + nit_empresa +
                ", direccion_empresa='" + direccion_empresa + '\'' +
                ", nombre_empresa='" + nombre_empresa + '\'' +
                ", telefono_empresa=" + telefono_empresa +
                ", email_empresa='" + email_empresa + '\'' +
                ", estado='" + estado + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
