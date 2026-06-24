package com.posta.datastructures;

import java.io.Serializable;

// Predicado de busqueda sobre los elementos de una estructura.
@FunctionalInterface
public interface Criterio<T> extends Serializable {

    boolean cumple(T elemento);
}
