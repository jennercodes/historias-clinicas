package com.posta.util;

// Validacion de DNI peruano: exactamente 8 digitos numericos.
public final class ValidadorDni {

    private ValidadorDni() {
    }

    public static boolean esValido(String dni) {
        return dni != null && dni.matches("\\d{8}");
    }
}
