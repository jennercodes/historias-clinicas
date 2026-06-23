package com.posta.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Conversion entre texto y fechas con el formato dd/MM/yyyy usado en la UI.
public final class FechaUtil {

    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private FechaUtil() {
    }

    // Devuelve la fecha o null si el texto no tiene el formato esperado.
    public static LocalDate parsear(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim(), FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String formatear(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FECHA);
    }

    public static String formatear(LocalDateTime fechaHora) {
        return fechaHora == null ? "" : fechaHora.format(FECHA_HORA);
    }
}
