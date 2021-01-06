package com.example.tenisclubdroid.ui.clases;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nickName,email,password,fotoPerfil,codigoQr,idUsuario,descripcion;
    private int rol;


    public Usuario(String nickName, String email, String password, String fotoPerfil, String idUsuario,int rol) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.fotoPerfil = fotoPerfil;
        this.idUsuario = idUsuario;
        this.rol=rol;
    }

    public Usuario(String nickName, String email, String password,String idUsuario, int rol) {
        this.nickName = nickName;
        this.idUsuario= idUsuario;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String nickName, String fotoPerfil, String descripcion, int rol) {
        this.nickName = nickName;
        this.fotoPerfil = fotoPerfil;
        this.descripcion = descripcion;
        this.rol= rol;
    }


    public Usuario(){

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                ", codigoQr='" + codigoQr + '\'' +
                ", idUsuario=" + idUsuario +
                ", rol=" + rol +
                '}';
    }
}
