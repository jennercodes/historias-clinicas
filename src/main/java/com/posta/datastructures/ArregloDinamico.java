package com.posta.datastructures;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArregloDinamico<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamano;

    public ArregloDinamico() {
        this(CAPACIDAD_INICIAL);
    }

    public ArregloDinamico(int capacidadInicial) {
        if (capacidadInicial < 1) {
            capacidadInicial = CAPACIDAD_INICIAL;
        }
        this.elementos = new Object[capacidadInicial];
        this.tamano = 0;
    }

    public void agregar(T elemento) {
        asegurarCapacidad(tamano + 1);
        elementos[tamano] = elemento;
        tamano++;
    }

    public void insertar(int indice, T elemento) {
        validarIndiceParaInsertar(indice);
        asegurarCapacidad(tamano + 1);
        for (int i = tamano; i > indice; i--) {
            elementos[i] = elementos[i - 1];
        }
        elementos[indice] = elemento;
        tamano++;
    }

    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        validarIndice(indice);
        return (T) elementos[indice];
    }

    @SuppressWarnings("unchecked")
    public T actualizar(int indice, T elemento) {
        validarIndice(indice);
        T anterior = (T) elementos[indice];
        elementos[indice] = elemento;
        return anterior;
    }

    @SuppressWarnings("unchecked")
    public T eliminar(int indice) {
        validarIndice(indice);
        T eliminado = (T) elementos[indice];
        for (int i = indice; i < tamano - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        elementos[tamano - 1] = null;
        tamano--;
        return eliminado;
    }

    // Elimina la primera coincidencia segun equals.
    public boolean eliminarElemento(T elemento) {
        int indice = indiceDe(elemento);
        if (indice == -1) {
            return false;
        }
        eliminar(indice);
        return true;
    }

    public void limpiar() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }
        tamano = 0;
    }

    public int indiceDe(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (sonIguales(elementos[i], elemento)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contiene(T elemento) {
        return indiceDe(elemento) != -1;
    }

    @SuppressWarnings("unchecked")
    public T buscar(Criterio<T> criterio) {
        for (int i = 0; i < tamano; i++) {
            T actual = (T) elementos[i];
            if (criterio.cumple(actual)) {
                return actual;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public int indiceDe(Criterio<T> criterio) {
        for (int i = 0; i < tamano; i++) {
            if (criterio.cumple((T) elementos[i])) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public ArregloDinamico<T> filtrar(Criterio<T> criterio) {
        ArregloDinamico<T> resultado = new ArregloDinamico<>();
        for (int i = 0; i < tamano; i++) {
            T actual = (T) elementos[i];
            if (criterio.cumple(actual)) {
                resultado.agregar(actual);
            }
        }
        return resultado;
    }

    // Copia superficial: mismas referencias de elementos.
    public ArregloDinamico<T> clonar() {
        ArregloDinamico<T> copia = new ArregloDinamico<>(Math.max(tamano, CAPACIDAD_INICIAL));
        for (int i = 0; i < tamano; i++) {
            copia.elementos[i] = elementos[i];
        }
        copia.tamano = tamano;
        return copia;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacio() {
        return tamano == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < tamano;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elementos[cursor++];
            }
        };
    }

    private void asegurarCapacidad(int requerida) {
        if (requerida <= elementos.length) {
            return;
        }
        int nuevaCapacidad = elementos.length * 2;
        if (nuevaCapacidad < requerida) {
            nuevaCapacidad = requerida;
        }
        Object[] nuevo = new Object[nuevaCapacidad];
        for (int i = 0; i < tamano; i++) {
            nuevo[i] = elementos[i];
        }
        elementos = nuevo;
    }

    private boolean sonIguales(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice " + indice + " fuera de rango [0, " + tamano + ")");
        }
    }

    private void validarIndiceParaInsertar(int indice) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Indice " + indice + " fuera de rango [0, " + tamano + "]");
        }
    }
}
