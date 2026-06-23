package com.posta.datastructures;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaDoble<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private NodoDoble<T> cabeza;
    private NodoDoble<T> cola;
    private int tamano;

    public ListaDoble() {
        this.cabeza = null;
        this.cola = null;
        this.tamano = 0;
    }

    // ---------------------------------------------------------------------
    // Insercion
    // ---------------------------------------------------------------------

    public void insertarInicio(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            nuevo.setSiguiente(cabeza);
            cabeza.setAnterior(nuevo);
            cabeza = nuevo;
        }
        tamano++;
    }

    public void insertarFinal(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);
        if (cola == null) {
            cabeza = cola = nuevo;
        } else {
            nuevo.setAnterior(cola);
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }
        tamano++;
    }

    // ---------------------------------------------------------------------
    // Acceso
    // ---------------------------------------------------------------------

    public T obtener(int indice) {
        return nodoEn(indice).getDato();
    }

    public T primero() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista esta vacia");
        }
        return cabeza.getDato();
    }

    public T ultimo() {
        if (cola == null) {
            throw new NoSuchElementException("La lista esta vacia");
        }
        return cola.getDato();
    }

    // ---------------------------------------------------------------------
    // Eliminacion
    // ---------------------------------------------------------------------

    /** Elimina la primera aparicion del dato (segun equals). */
    public boolean eliminar(T dato) {
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                desenlazar(actual);
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void limpiar() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    // ---------------------------------------------------------------------
    // Busqueda
    // ---------------------------------------------------------------------

    public boolean contiene(T dato) {
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public T buscar(Criterio<T> criterio) {
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (criterio.cumple(actual.getDato())) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public ListaDoble<T> filtrar(Criterio<T> criterio) {
        ListaDoble<T> resultado = new ListaDoble<>();
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (criterio.cumple(actual.getDato())) {
                resultado.insertarFinal(actual.getDato());
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    // ---------------------------------------------------------------------
    // Estado
    // ---------------------------------------------------------------------

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    // ---------------------------------------------------------------------
    // Recorrido hacia adelante (cabeza -> cola)
    // ---------------------------------------------------------------------

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private NodoDoble<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    /**
     * Iterador en sentido inverso (cola -> cabeza), util para mostrar el
     * historial de atenciones de la mas reciente a la mas antigua.
     */
    public Iterable<T> enReversa() {
        return () -> new Iterator<>() {
            private NodoDoble<T> actual = cola;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getAnterior();
                return dato;
            }
        };
    }

    // ---------------------------------------------------------------------
    // Apoyo interno
    // ---------------------------------------------------------------------

    private void desenlazar(NodoDoble<T> nodo) {
        NodoDoble<T> anterior = nodo.getAnterior();
        NodoDoble<T> siguiente = nodo.getSiguiente();

        if (anterior == null) {
            cabeza = siguiente;
        } else {
            anterior.setSiguiente(siguiente);
        }

        if (siguiente == null) {
            cola = anterior;
        } else {
            siguiente.setAnterior(anterior);
        }

        nodo.setAnterior(null);
        nodo.setSiguiente(null);
        tamano--;
    }

    private NodoDoble<T> nodoEn(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice " + indice + " fuera de rango [0, " + tamano + ")");
        }
        NodoDoble<T> actual;
        // Recorre desde el extremo mas cercano al indice
        if (indice < tamano / 2) {
            actual = cabeza;
            for (int i = 0; i < indice; i++) {
                actual = actual.getSiguiente();
            }
        } else {
            actual = cola;
            for (int i = tamano - 1; i > indice; i--) {
                actual = actual.getAnterior();
            }
        }
        return actual;
    }

    private boolean sonIguales(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}
