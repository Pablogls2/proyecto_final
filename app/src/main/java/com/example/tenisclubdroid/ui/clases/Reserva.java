package com.example.tenisclubdroid.ui.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Reserva implements Serializable {

    private Pista pista;
    private ArrayList<Integer > extras;
    private String idReservador,idAdversario,idReserva,fecha;
    private int precio;


    public Reserva(Pista pista) {
        this.pista = pista;
    }

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
    }

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, String idAdversario) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.idAdversario = idAdversario;
    }

    public Reserva(Pista pista,String fecha, ArrayList<Integer> extras, String idReservador) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.fecha = fecha;
    }

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, String idAdversario, String fecha) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.idAdversario = idAdversario;
        this.fecha = fecha;
    }

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, String idAdversario, String idReserva, String  fecha) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.idAdversario = idAdversario;
        this.idReserva = idReserva;
        this.fecha = fecha;
    }





    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, String idAdversario, String idReserva, int precio, String fecha) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.idAdversario = idAdversario;
        this.idReserva = idReserva;
        this.precio = precio;
        this.fecha = fecha;
    }

    public Pista getPista() {
        return pista;
    }

    public void setPista(Pista pista) {
        this.pista = pista;
    }

    public ArrayList<Integer> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<Integer> extras) {
        this.extras = extras;
    }

    public String getIdReservador() {
        return idReservador;
    }

    public void setIdReservador(String idReservador) {
        this.idReservador = idReservador;
    }

    public String getIdAdversario() {
        return idAdversario;
    }

    public void setIdAdversario(String idAdversario) {
        this.idAdversario = idAdversario;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
