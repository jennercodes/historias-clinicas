package com.posta.datastructures;

import java.io.Serializable;

/**
 * Nodo de una lista enlazada simple. Almacena un dato y la referencia
 * al siguiente nodo de la lista.
 *
 * @param <T> tipo de dato almacenado
 */
public class Nodo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T dato;
    private Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
