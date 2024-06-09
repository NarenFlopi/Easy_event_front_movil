package com.example.easy_event_app.model;

import java.util.List;

public class ProductoRespuesta {


    private Long alquiler_id;
    private String precio_alquiler;

    public Long getAlquiler_id() {
        return alquiler_id;
    }

    public void setAlquiler_id(Long alquiler_id) {
        this.alquiler_id = alquiler_id;
    }

    public String getPrecio_alquiler() {
        return precio_alquiler;
    }

    public void setPrecio_alquiler(String precio_alquiler) {
        this.precio_alquiler = precio_alquiler;
    }

    private List<Producto> Producto;

    public List<Producto> getProducto() {
        return Producto;
    }

    public void setProducto(List<com.example.easy_event_app.model.Producto> producto) {
        Producto = producto;
    }

    @Override
    public String toString() {
        return "ProductoRespuesta{" +
                "alquiler_id=" + alquiler_id +
                ", precio_alquiler='" + precio_alquiler + '\'' +
                '}';
    }
}
