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

    public Paciente porDni(String dni) {
        return repositorio.buscarPorDni(dni);
    }

    public ArregloDinamico<Paciente> porNombre(String texto) {
        return repositorio.buscarPorNombre(texto);
    }
}
