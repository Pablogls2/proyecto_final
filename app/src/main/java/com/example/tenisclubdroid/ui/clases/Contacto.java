package com.example.tenisclubdroid.ui.clases;

import java.io.Serializable;

public class Contacto implements Serializable {
    private String nickName,fotoPerfil,idUsuario,descripcion;

    public Contacto(String nickName, String fotoPerfil, String idUsuario) {
        this.nickName = nickName;
        this.fotoPerfil = fotoPerfil;
        this.idUsuario = idUsuario;
    }

    public Contacto(String nickName, String fotoPerfil, String idUsuario, String descripcion) {
        this.nickName = nickName;
        this.fotoPerfil = fotoPerfil;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
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

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "nickName='" + nickName + '\'' +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                '}';
    }
}
