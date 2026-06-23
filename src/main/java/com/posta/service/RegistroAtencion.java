package com.posta.service;

import com.posta.model.Atencion;

// Fila de resultado de una consulta de atenciones: vincula la atencion con la
// historia y el paciente a los que pertenece (usado en los reportes RF08/RF09).
public record RegistroAtencion(int historiaId, int pacienteId, Atencion atencion) {
}
