package com.posta.datastructures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ListaDobleTest {

    @Test
    void nuevaListaEstaVacia() {
        ListaDoble<String> l = new ListaDoble<>();
        assertTrue(l.estaVacia());
        assertEquals(0, l.tamano());
    }

    @Test
    void insertarFinalMantieneOrdenYExtremos() {
        ListaDoble<String> l = new ListaDoble<>();
        l.insertarFinal("a");
        l.insertarFinal("b");
        l.insertarFinal("c");
        assertEquals("a", l.primero());
        assertEquals("c", l.ultimo());
        assertEquals("b", l.obtener(1));
        assertEquals(3, l.tamano());
    }

    @Test
    void insertarInicioActualizaCabeza() {
        ListaDoble<String> l = new ListaDoble<>();
        l.insertarFinal("b");
        l.insertarInicio("a");
        assertEquals("a", l.primero());
        assertEquals("b", l.ultimo());
    }

    @Test
    void recorridoAdelante() {
        ListaDoble<Integer> l = new ListaDoble<>();
        l.insertarFinal(1);
        l.insertarFinal(2);
        l.insertarFinal(3);
        List<Integer> vistos = new ArrayList<>();
        for (int v : l) {
            vistos.add(v);
        }
        assertEquals(List.of(1, 2, 3), vistos);
    }

    @Test
    void recorridoEnReversa() {
        ListaDoble<Integer> l = new ListaDoble<>();
        l.insertarFinal(1);
        l.insertarFinal(2);
        l.insertarFinal(3);
        List<Integer> vistos = new ArrayList<>();
        for (int v : l.enReversa()) {
            vistos.add(v);
        }
        assertEquals(List.of(3, 2, 1), vistos);
    }

    @Test
    void eliminarCabezaMedioYCola() {
        ListaDoble<String> l = new ListaDoble<>();
        l.insertarFinal("a");
        l.insertarFinal("b");
        l.insertarFinal("c");

        assertTrue(l.eliminar("b")); // medio
        assertEquals("a", l.primero());
        assertEquals("c", l.ultimo());
        assertEquals(2, l.tamano());

        assertTrue(l.eliminar("a")); // cabeza
        assertEquals("c", l.primero());
        assertEquals("c", l.ultimo());

        assertTrue(l.eliminar("c")); // cola (unico restante)
        assertTrue(l.estaVacia());
    }

    @Test
    void eliminarInexistenteDevuelveFalse() {
        ListaDoble<String> l = new ListaDoble<>();
        l.insertarFinal("a");
        assertFalse(l.eliminar("z"));
        assertEquals(1, l.tamano());
    }

    @Test
    void enlacesBidireccionalesConsistentesTrasEliminar() {
        // Verifica que el recorrido en reversa siga integro despues de borrar el medio
        ListaDoble<Integer> l = new ListaDoble<>();
        l.insertarFinal(1);
        l.insertarFinal(2);
        l.insertarFinal(3);
        l.insertarFinal(4);
        l.eliminar(3);

        List<Integer> adelante = new ArrayList<>();
        for (int v : l) adelante.add(v);
        assertEquals(List.of(1, 2, 4), adelante);

        List<Integer> atras = new ArrayList<>();
        for (int v : l.enReversa()) atras.add(v);
        assertEquals(List.of(4, 2, 1), atras);
    }

    @Test
    void buscarYContiene() {
        ListaDoble<Integer> l = new ListaDoble<>();
        l.insertarFinal(5);
        l.insertarFinal(20);
        assertTrue(l.contiene(20));
        assertEquals(20, l.buscar(n -> n > 10));
        assertNull(l.buscar(n -> n > 100));
    }

    @Test
    void obtenerEnListaVaciaLanzaExcepcion() {
        ListaDoble<String> l = new ListaDoble<>();
        assertThrows(NoSuchElementException.class, l::primero);
        assertThrows(NoSuchElementException.class, l::ultimo);
        assertThrows(IndexOutOfBoundsException.class, () -> l.obtener(0));
    }
}
