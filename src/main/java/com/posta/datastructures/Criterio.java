package com.posta.datastructures;

import java.io.Serializable;

/**
 * Criterio de busqueda sobre los elementos de una estructura de datos.
 * Se usa para localizar elementos que cumplan una condicion sin acoplar
 * la estructura a un tipo concreto (por ejemplo: buscar el paciente cuyo
 * DNI coincide con un valor).
 *
 * @param <T> tipo de elemento evaluado
 */
@FunctionalInterface
public interface Criterio<T> extends Serializable {

    /**
     * @param elemento elemento a evaluar
     * @return true si el elemento cumple la condicion de busqueda
     */
    boolean cumple(T elemento);
}
