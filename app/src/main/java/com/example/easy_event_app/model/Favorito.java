package com.example.easy_event_app.model;

import com.example.easy_event_app.Productos;

public class Favorito {

    private long id;
    private long producto_id;
    private long user_id;
    private Producto producto;

    public Favorito(long id, long producto_id, long user_id, Producto producto) {
        this.id = id;
        this.producto_id = producto_id;
        this.user_id = user_id;
        this.producto = producto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(long producto_id) {
        this.producto_id = producto_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
