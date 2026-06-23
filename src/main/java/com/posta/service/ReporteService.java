package com.posta.service;

import com.posta.datastructures.ArregloDinamico;
import com.posta.model.Atencion;
import com.posta.model.HistoriaClinica;
import com.posta.repository.RepositorioHistoria;

import java.time.LocalDate;

// Reportes de atenciones por especialidad y rango de fechas (RF09).
public class ReporteService {

    private final RepositorioHistoria repositorioHistoria;

    public ReporteService(RepositorioHistoria repositorioHistoria) {
        this.repositorioHistoria = repositorioHistoria;
    }

    // Atenciones de una especialidad realizadas entre dos fechas (inclusive).
    public ArregloDinamico<RegistroAtencion> porEspecialidadYRango(int especialidadId,
                                                                   LocalDate desde,
                                                                   LocalDate hasta) {
        ArregloDinamico<RegistroAtencion> resultado = new ArregloDinamico<>();
        for (HistoriaClinica historia : repositorioHistoria.listar()) {
            for (Atencion atencion : historia.getAtenciones()) {
                if (atencion.getEspecialidadId() == especialidadId
                        && dentroDelRango(atencion.getFechaHora().toLocalDate(), desde, hasta)) {
                    resultado.agregar(new RegistroAtencion(
                            historia.getId(), historia.getPacienteId(), atencion));
                }
            }
        }
        return resultado;
    }

    private boolean dentroDelRango(LocalDate fecha, LocalDate desde, LocalDate hasta) {
        return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }
}
