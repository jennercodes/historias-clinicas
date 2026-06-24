package com.posta.repository;

import com.posta.datastructures.ArregloDinamico;
import com.posta.model.Medico;

public class RepositorioMedico extends RepositorioArreglo<Medico> {

    public RepositorioMedico() {
        super("medicos.dat");
    }

    @Override
    protected int idDe(Medico m) {
        return m.getId();
    }

    @Override
    protected void asignarId(Medico m, int id) {
        m.setId(id);
    }

    public Medico buscarPorDni(String dni) {
        if (dni == null) {
            return null;
        }
        return datos.buscar(m -> dni.equals(m.getDni()));
    }

    public ArregloDinamico<Medico> listarPorEspecialidad(int especialidadId) {
        return datos.filtrar(m -> m.getEspecialidadId() == especialidadId);
    }
}
