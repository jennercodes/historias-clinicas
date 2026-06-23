package com.posta.model;

// Rol del usuario que accede al sistema (controla el login, RF01/RF04).
public enum Rol {
    ADMINISTRADOR("Administrador"),
    OPERADOR("Operador");

    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
