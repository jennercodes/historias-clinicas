package com.posta.repository;

import com.posta.datastructures.ArregloDinamico;
import com.posta.model.Paciente;

// Busquedas por DNI y por nombre/apellidos (RF07).
public class RepositorioPaciente extends RepositorioArreglo<Paciente> {

    public RepositorioPaciente() {
        super("pacientes.dat");
    }

    @Override
    protected int idDe(Paciente p) {
        return p.getId();
    }

    @Override
    protected void asignarId(Paciente p, int id) {
        p.setId(id);
    }

    public Paciente buscarPorDni(String dni) {
        if (dni == null) {
            return null;
        }
        return datos.buscar(p -> dni.equals(p.getDni()));
    }

    // Coincidencia parcial sin distinguir mayusculas, sobre nombres o apellidos.
    public ArregloDinamico<Paciente> buscarPorNombre(String texto) {
        if (texto == null || texto.isBlank()) {
            return datos.clonar();
        }
        String aguja = texto.toLowerCase();
        return datos.filtrar(p ->
                p.getNombres().toLowerCase().contains(aguja)
                        || p.getApellidos().toLowerCase().contains(aguja));
    }
}
