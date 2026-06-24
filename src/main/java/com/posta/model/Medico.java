package com.posta.model;

import java.io.Serializable;
import java.util.Objects;

// Clave natural: DNI. La especialidad se referencia por id (no por objeto) para
// no duplicarla al persistir.
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String colegiatura; // numero de colegiatura medica (CMP)
    private String telefono;
    private int especialidadId;

    public Medico() {
    }

    public Medico(int id, String dni, String nombres, String apellidos,
                  String colegiatura, String telefono, int especialidadId) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.colegiatura = colegiatura;
        this.telefono = telefono;
        this.especialidadId = especialidadId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico otro)) return false;
        return Objects.equals(dni, otro.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}
