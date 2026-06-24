package com.posta.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

// Clave natural: DNI.
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private Sexo sexo;
    private String direccion;
    private String telefono;

    public Paciente() {
    }

    public Paciente(int id, String dni, String nombres, String apellidos,
                    LocalDate fechaNacimiento, Sexo sexo, String direccion, String telefono) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // Edad calculada a la fecha actual.
    public int getEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente otro)) return false;
        return Objects.equals(dni, otro.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (DNI " + dni + ")";
    }
}
