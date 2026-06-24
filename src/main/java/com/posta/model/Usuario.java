package com.posta.model;

import java.io.Serializable;
import java.util.Objects;

// Clave natural: usuario.
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String usuario;
    private String clave;
    private String nombreCompleto;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(int id, String usuario, String clave, String nombreCompleto, Rol rol) {
        this.id = id;
        this.usuario = usuario;
        this.clave = clave;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario otro)) return false;
        return Objects.equals(usuario, otro.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usuario);
    }

    @Override
    public String toString() {
        return usuario + " (" + rol + ")";
    }
}
