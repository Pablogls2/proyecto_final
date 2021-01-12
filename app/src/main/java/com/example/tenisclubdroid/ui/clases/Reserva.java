package com.example.tenisclubdroid.ui.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Reserva implements Serializable {

    private Pista pista;
    private ArrayList<Integer > extras;
    private String idReservador,idAdversario,idReserva;
    private int precio;
    private Date fecha;

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

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, Date fecha) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.fecha = fecha;
    }

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, String idAdversario, Date fecha) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.idAdversario = idAdversario;
        this.fecha = fecha;
    }

    public Reserva(Pista pista, ArrayList<Integer> extras, String idReservador, String idAdversario, String idReserva, Date fecha) {
        this.pista = pista;
        this.extras = extras;
        this.idReservador = idReservador;
        this.idAdversario = idAdversario;
        this.idReserva = idReserva;
        this.fecha = fecha;
    }




    public Reserva(Pista pista, ArrayList<String> Integer, String idReservador, String idAdversario, String idReserva, int precio, Date fecha) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
