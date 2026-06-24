package com.posta.service;

import com.posta.datastructures.ArregloDinamico;
import com.posta.model.Atencion;
import com.posta.model.HistoriaClinica;
import com.posta.model.Paciente;
import com.posta.repository.RepositorioHistoria;
import com.posta.repository.RepositorioPaciente;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Registro de atenciones medicas y consultas relacionadas (RF06, RF08).
public class AtencionService {

    private final RepositorioHistoria repositorioHistoria;
    private final RepositorioPaciente repositorioPaciente;

    public AtencionService(RepositorioHistoria repositorioHistoria,
                           RepositorioPaciente repositorioPaciente) {
        this.repositorioHistoria = repositorioHistoria;
        this.repositorioPaciente = repositorioPaciente;
    }

    // Historia clinica del paciente; la abre si aun no existe.
    public HistoriaClinica obtenerOAbrirHistoria(int pacienteId) {
        Paciente paciente = repositorioPaciente.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new IllegalArgumentException("No existe un paciente con id " + pacienteId);
        }
        HistoriaClinica historia = repositorioHistoria.buscarPorPacienteId(pacienteId);
        if (historia == null) {
            historia = repositorioHistoria.guardar(
                    new HistoriaClinica(0, pacienteId, LocalDate.now()));
        }
        return historia;
    }

    // Registra una nueva atencion en la historia del paciente (RF06).
    public Atencion registrar(int pacienteId, int medicoId, int especialidadId,
                              LocalDateTime fechaHora, String motivo, String diagnostico,
                              String tratamiento, String observaciones) {
        HistoriaClinica historia = obtenerOAbrirHistoria(pacienteId);
        Atencion atencion = new Atencion(siguienteIdAtencion(), fechaHora, medicoId,
                especialidadId, motivo, diagnostico, tratamiento, observaciones);
        historia.agregarAtencion(atencion);
        repositorioHistoria.guardarCambios();
        return atencion;
    }

    // Atenciones realizadas por un medico en una fecha (RF08).
    public ArregloDinamico<RegistroAtencion> atencionesDelDia(int medicoId, LocalDate dia) {
        ArregloDinamico<RegistroAtencion> resultado = new ArregloDinamico<>();
        for (HistoriaClinica historia : repositorioHistoria.listar()) {
            for (Atencion atencion : historia.getAtenciones()) {
                if (atencion.getMedicoId() == medicoId
                        && atencion.getFechaHora().toLocalDate().equals(dia)) {
                    resultado.agregar(new RegistroAtencion(
                            historia.getId(), historia.getPacienteId(), atencion));
                }
            }
        }
        return resultado;
    }

    // Siguiente id de atencion: mayor existente + 1 (ids globales para los reportes).
    private int siguienteIdAtencion() {
        int maximo = 0;
        for (HistoriaClinica historia : repositorioHistoria.listar()) {
            for (Atencion atencion : historia.getAtenciones()) {
                maximo = Math.max(maximo, atencion.getId());
            }
        }
        return maximo + 1;
    }
}
