package com.posta.repository;

import com.posta.model.Especialidad;

// Repositorio de especialidades medicas. Siembra un conjunto base la primera
// vez que se ejecuta la aplicacion.
public class RepositorioEspecialidad extends RepositorioArreglo<Especialidad> {

    public RepositorioEspecialidad() {
        super("especialidades.dat");
        if (datos.estaVacio()) {
            sembrarBase();
        }
    }

    @Override
    protected int idDe(Especialidad e) {
        return e.getId();
    }

    @Override
    protected void asignarId(Especialidad e, int id) {
        e.setId(id);
    }

    public Especialidad buscarPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        return datos.buscar(e -> nombre.equalsIgnoreCase(e.getNombre()));
    }

    private void sembrarBase() {
        guardar(new Especialidad(0, "Medicina General", "Atencion primaria y consultas generales"));
        guardar(new Especialidad(0, "Pediatria", "Atencion de ninos y adolescentes"));
        guardar(new Especialidad(0, "Ginecologia", "Salud de la mujer"));
        guardar(new Especialidad(0, "Odontologia", "Salud bucal"));
        guardar(new Especialidad(0, "Cardiologia", "Enfermedades del corazon"));
    }
}
