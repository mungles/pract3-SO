package com.lamejorcompaiadeluniberso.so;

/**
 * Created by axel on 04/12/2016.
 */

public class Proceso {
    String nombre;
    int llegada;
    int memoria;
    int tiempo;

    public Proceso(String nombre, int llegada, int memoria, int tiempo) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.memoria = memoria;
        this.tiempo = tiempo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getLlegada(){
        return this.llegada;
    }

    public int getMemoria(){
        return this.memoria;
    }

    public int getTiempo(){
        return this.tiempo;
    }

    public String toString(){
        return "Nombre: " + this.nombre + ", llegada: " + this.llegada + ", memoria requerida: " + this.memoria + ", tiempo ejecucion: " + this.tiempo;
    }
}