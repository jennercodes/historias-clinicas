package com.posta.datastructures;

import java.io.Serializable;

/**
 * Nodo de una lista doblemente enlazada. Mantiene referencias al nodo
 * anterior y al siguiente, lo que permite recorrer el historial en ambos
 * sentidos.
 *
 * @param <T> tipo de dato almacenado
 */
public class NodoDoble<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T dato;
    private NodoDoble<T> anterior;
    private NodoDoble<T> siguiente;

    public NodoDoble(T dato) {
        this.dato = dato;
        this.anterior = null;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoDoble<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoDoble<T> anterior) {
        this.anterior = anterior;
    }

    public NodoDoble<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDoble<T> siguiente) {
        this.siguiente = siguiente;
    }
}
