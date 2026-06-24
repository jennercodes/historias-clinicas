package com.posta.repository;

import com.posta.datastructures.ListaSimple;
import com.posta.model.HistoriaClinica;

// Respaldado por una lista enlazada simple porque la cantidad de historias crece
// dinamicamente (marco teorico 2.2.6). Cada HistoriaClinica contiene su ListaDoble
// de atenciones, asi que al persistir la lista se guarda tambien el historial.
public class RepositorioHistoria extends RepositorioBase<ListaSimple<HistoriaClinica>> {

    public RepositorioHistoria() {
        super("historias.dat", ListaSimple::new);
    }

    // Lista interna: las historias son objetos vivos que la UI modifica (luego se
    // persiste con guardarCambios).
    public ListaSimple<HistoriaClinica> listar() {
        return datos;
    }

    public int cantidad() {
        return datos.tamano();
    }

    public HistoriaClinica buscarPorId(int id) {
        return datos.buscar(h -> h.getId() == id);
    }

    public HistoriaClinica buscarPorPacienteId(int pacienteId) {
        return datos.buscar(h -> h.getPacienteId() == pacienteId);
    }

    // Alta o reemplazo por id. Asigna id nuevo si la historia no lo tiene.
    public HistoriaClinica guardar(HistoriaClinica historia) {
        if (historia.getId() == 0) {
            historia.setId(siguienteId());
            datos.insertarFinal(historia);
        } else {
            datos.eliminar(historia); // equals por id: quita la version anterior
            datos.insertarFinal(historia);
        }
        persistir();
        return historia;
    }

    // Persiste tras modificar en memoria una historia ya almacenada (p.ej. al
    // agregarle una atencion).
    public void guardarCambios() {
        persistir();
    }

    private int siguienteId() {
        int maximo = 0;
        for (HistoriaClinica h : datos) {
            maximo = Math.max(maximo, h.getId());
        }
        return maximo + 1;
    }
}
