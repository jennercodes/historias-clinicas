package com.posta.datastructures;

import java.io.Serializable;

@FunctionalInterface
public interface Criterio<T> extends Serializable {

    /**
     * @param elemento elemento a evaluar
     * @return true si el elemento cumple la condicion de busqueda
     */
    boolean cumple(T elemento);
}
