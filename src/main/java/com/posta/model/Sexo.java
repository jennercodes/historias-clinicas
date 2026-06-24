package com.posta.model;

public enum Sexo {
    MASCULINO("Masculino"),
    FEMENINO("Femenino");

    private final String etiqueta;

    Sexo(String etiqueta) {
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
