package com.lamejorcompaiadeluniberso.so;

/**
 * Created by sergio on 4/12/16.
 */

public class Particion {
    private int inicio;
    private int tamaño;
    private String estado;
    private boolean libre;
    private int ttl;

    public Particion(int inicio, int tamaño, String estado, boolean libre, int ttl) {
        this.inicio = inicio;
        this.tamaño = tamaño;
        this.estado = estado;
        this.libre = libre;
        this.ttl = ttl;
    }

    public Particion(Particion p) {
        this.inicio = p.getInicio();
        this.tamaño = p.getTamaño();
        this.estado = p.getEstado();
        this.libre = p.isLibre();
        this.ttl = p.getTtl();
    }

    public int getInicio() { return inicio; }
    public int getTamaño() { return tamaño; }
    public String getEstado() { return estado; }
    public boolean isLibre() { return libre; }
    public int getTtl() { return ttl; }

    public void setInicio(int inicio) { this.inicio = inicio; }
    public void setTamaño(int tamaño) { this.tamaño = tamaño; }
    public void setEstado(String estado) { this.estado = estado; }
    public void reservar() { this.libre = false; }
    public void liberar() { this.libre = true; }

    @Override
    public String toString() {
        return "[" + this.inicio + " " + this.estado + " " + this.tamaño + "]";
    }
}
