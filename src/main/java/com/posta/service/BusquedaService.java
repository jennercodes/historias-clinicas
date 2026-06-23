package com.posta.service;

import com.posta.datastructures.ArregloDinamico;
import com.posta.model.Paciente;
import com.posta.repository.RepositorioPaciente;

// Busqueda de pacientes por DNI o por nombre/apellidos (RF07).
public class BusquedaService {

    private final RepositorioPaciente repositorio;

    public BusquedaService(RepositorioPaciente repositorio) {
        this.repositorio = repositorio;
    }

    // Busqueda exacta por DNI; null si no existe.
    public Paciente porDni(String dni) {
        return repositorio.buscarPorDni(dni);
    }

    // Coincidencia parcial sobre nombres o apellidos (sin distinguir mayusculas).
    public ArregloDinamico<Paciente> porNombre(String texto) {
        return repositorio.buscarPorNombre(texto);
    }
}
