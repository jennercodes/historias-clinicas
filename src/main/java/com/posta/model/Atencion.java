package com.posta.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

// Atencion medica registrada dentro de una historia clinica.
// Referencia a medico y especialidad por id (no por objeto) para no duplicar
// datos al persistir. Es el nodo logico de la ListaDoble del historial.
public class Atencion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDateTime fechaHora;
    private int medicoId;
    private int especialidadId;
    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    public Atencion() {
    }

    public Atencion(int id, LocalDateTime fechaHora, int medicoId, int especialidadId,
                    String motivo, String diagnostico, String tratamiento, String observaciones) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.medicoId = medicoId;
        this.especialidadId = especialidadId;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public int getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atencion otra)) return false;
        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Atencion #" + id + " - " + motivo;
    }
}
