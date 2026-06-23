package com.posta.model;

import java.io.Serializable;
import java.util.Objects;

// Especialidad medica. Clave natural: id (generado por el repositorio).
public class Especialidad implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String descripcion;

    public Especialidad() {
    }

    public Especialidad(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Especialidad otra)) return false;
        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
