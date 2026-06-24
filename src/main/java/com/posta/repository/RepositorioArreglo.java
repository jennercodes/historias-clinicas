package com.posta.repository;

import com.posta.datastructures.ArregloDinamico;

// Repositorios respaldados por un ArregloDinamico, con CRUD por id
// autoincremental. Cada subclase indica como leer y asignar el id de su entidad.
public abstract class RepositorioArreglo<T> extends RepositorioBase<ArregloDinamico<T>> {

    protected RepositorioArreglo(String nombreArchivo) {
        super(nombreArchivo, ArregloDinamico::new);
    }

    protected abstract int idDe(T entidad);

    protected abstract void asignarId(T entidad, int id);

    // Copia del listado (no expone la estructura interna).
    public ArregloDinamico<T> listar() {
        return datos.clonar();
    }

    public int cantidad() {
        return datos.tamano();
    }

    public T buscarPorId(int id) {
        return datos.buscar(e -> idDe(e) == id);
    }

    // Alta o actualizacion: si la entidad no tiene id (0) se le asigna uno nuevo;
    // si ya tiene id, se reemplaza la existente.
    public T guardar(T entidad) {
        if (idDe(entidad) == 0) {
            asignarId(entidad, siguienteId());
            datos.agregar(entidad);
        } else {
            int indice = datos.indiceDe(e -> idDe(e) == idDe(entidad));
            if (indice >= 0) {
                datos.actualizar(indice, entidad);
            } else {
                datos.agregar(entidad);
            }
        }
        persistir();
        return entidad;
    }

    public boolean eliminar(int id) {
        int indice = datos.indiceDe(e -> idDe(e) == id);
        if (indice < 0) {
            return false;
        }
        datos.eliminar(indice);
        persistir();
        return true;
    }

    // Siguiente id: uno mas que el mayor id existente.
    protected int siguienteId() {
        int maximo = 0;
        for (T entidad : datos) {
            maximo = Math.max(maximo, idDe(entidad));
        }
        return maximo + 1;
    }

    // Persiste tras modificar en memoria una entidad ya almacenada.
    public void guardarCambios() {
        persistir();
    }
}
