package com.example.easy_event_app.model;

public class ContadorAlquileres {

    private long alquileres_solicitados;
    private long alquileres_entregados;

    public ContadorAlquileres(long alquileres_solicitados, long alquileres_entregados) {
        this.alquileres_solicitados = alquileres_solicitados;
        this.alquileres_entregados = alquileres_entregados;
    }

    public long getAlquileres_solicitados() {
        return alquileres_solicitados;
    }

    public void setAlquileres_solicitados(long alquileres_solicitados) {
        this.alquileres_solicitados = alquileres_solicitados;
    }

    public long getAlquileres_entregados() {
        return alquileres_entregados;
    }

    public void setAlquileres_entregados(long alquileres_entregados) {
        this.alquileres_entregados = alquileres_entregados;
    }

    @Override
    public String toString() {
        return "ContadorAlquileres{" +
                "alquileres_solicitados=" + alquileres_solicitados +
                ", alquileres_entregados=" + alquileres_entregados +
                '}';
    }
}
