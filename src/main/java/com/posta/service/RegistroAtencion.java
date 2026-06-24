package com.posta.service;

import com.posta.model.Atencion;

// Fila de reporte: vincula una atencion con su historia y paciente (RF08/RF09).
public record RegistroAtencion(int historiaId, int pacienteId, Atencion atencion) {
}
