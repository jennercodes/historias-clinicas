package com.posta.datastructures;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaCircular<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private Nodo<T> ultimo; // referencia al ultimo nodo; ultimo.siguiente = cabeza
    private int tamano;

    public ListaCircular() {
        this.ultimo = null;
        this.tamano = 0;
    }

    /** Agrega un elemento al final del ciclo. */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (ultimo == null) {
            nuevo.setSiguiente(nuevo); // se apunta a si mismo
            ultimo = nuevo;
        } else {
            nuevo.setSiguiente(ultimo.getSiguiente()); // apunta a la cabeza
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        tamano++;
    }

    /** Elimina la primera aparicion del dato (segun equals). */
    public boolean eliminar(T dato) {
        if (ultimo == null) {
            return false;
        }
        Nodo<T> anterior = ultimo;
        Nodo<T> actual = ultimo.getSiguiente(); // cabeza
        for (int i = 0; i < tamano; i++) {
            if (sonIguales(actual.getDato(), dato)) {
                if (actual == ultimo && actual.getSiguiente() == actual) {
                    // unico elemento
                    ultimo = null;
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                    if (actual == ultimo) {
                        ultimo = anterior;
                    }
                }
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    public T buscar(Criterio<T> criterio) {
        if (ultimo == null) {
            return null;
        }
        Nodo<T> actual = ultimo.getSiguiente();
        for (int i = 0; i < tamano; i++) {
            if (criterio.cumple(actual.getDato())) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    /** Recorrido acotado al numero de elementos (una vuelta completa). */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo<T> actual = (ultimo == null) ? null : ultimo.getSiguiente();
            private int visitados = 0;

            @Override
            public boolean hasNext() {
                return visitados < tamano;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                visitados++;
                return dato;
            }
        };
    }

    private boolean sonIguales(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}
