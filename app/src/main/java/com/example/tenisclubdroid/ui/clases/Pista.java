package com.example.tenisclubdroid.ui.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Pista implements Serializable {
    private String foto,nombre;
    private int precio;
    private ArrayList<Integer> extras;

    public Pista(String foto, String nombre, int precio, ArrayList<Integer> extras) {
        this.foto = foto;
        this.nombre = nombre;
        this.precio = precio;
        this.extras = extras;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public ArrayList<Integer> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<Integer> extras) {
        this.extras = extras;
    }
}
