package com.posta.repository;

// Error no recuperable al leer o escribir los archivos de datos.
public class RepositorioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
