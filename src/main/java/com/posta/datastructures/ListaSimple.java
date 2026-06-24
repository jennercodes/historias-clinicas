package com.posta.datastructures;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaSimple<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private Nodo<T> cabeza;
    private int tamano;

    public ListaSimple() {
        this.cabeza = null;
        this.tamano = 0;
    }

    public void insertarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamano++;
    }

    public void insertarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    public T obtener(int indice) {
        return nodoEn(indice).getDato();
    }

    public T primero() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista esta vacia");
        }
        return cabeza.getDato();
    }

    public T eliminarInicio() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista esta vacia");
        }
        T dato = cabeza.getDato();
        cabeza = cabeza.getSiguiente();
        tamano--;
        return dato;
    }

    // Elimina la primera coincidencia segun equals.
    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }
        if (sonIguales(cabeza.getDato(), dato)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            return true;
        }
        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                anterior.setSiguiente(actual.getSiguiente());
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void limpiar() {
        cabeza = null;
        tamano = 0;
    }

    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public T buscar(Criterio<T> criterio) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (criterio.cumple(actual.getDato())) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public ListaSimple<T> filtrar(Criterio<T> criterio) {
        ListaSimple<T> resultado = new ListaSimple<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (criterio.cumple(actual.getDato())) {
                resultado.insertarFinal(actual.getDato());
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo<T> actual = cabeza;

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

    private Nodo<T> nodoEn(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice " + indice + " fuera de rango [0, " + tamano + ")");
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }

    private boolean sonIguales(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}
