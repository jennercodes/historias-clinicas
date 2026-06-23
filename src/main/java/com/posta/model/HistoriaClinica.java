package com.posta.model;

import com.posta.datastructures.ListaDoble;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// Historia clinica de un paciente. Clave natural: id.
// El paciente se referencia por id. Las atenciones se guardan en una lista
// doblemente enlazada para poder recorrer el historial en ambos sentidos
// (marco teorico 2.2.8).
public class HistoriaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int pacienteId;
    private LocalDate fechaApertura;
    private String antecedentes;
    private String alergias;
    private String grupoSanguineo;
    private final ListaDoble<Atencion> atenciones;

    public HistoriaClinica() {
        this.atenciones = new ListaDoble<>();
    }

    public HistoriaClinica(int id, int pacienteId, LocalDate fechaApertura) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.fechaApertura = fechaApertura;
        this.atenciones = new ListaDoble<>();
    }

    // Agrega una atencion al final del historial.
    public void agregarAtencion(Atencion atencion) {
        atenciones.insertarFinal(atencion);
    }

    public ListaDoble<Atencion> getAtenciones() {
        return atenciones;
    }

    public int cantidadAtenciones() {
        return atenciones.tamano();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoriaClinica otra)) return false;
        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HC #" + id + " (paciente " + pacienteId + ")";
    }
}
