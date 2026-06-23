package com.posta.datastructures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ListaSimpleTest {

    @Test
    void nuevaListaEstaVacia() {
        ListaSimple<String> l = new ListaSimple<>();
        assertTrue(l.estaVacia());
        assertEquals(0, l.tamano());
    }

    @Test
    void insertarFinalMantieneOrden() {
        ListaSimple<String> l = new ListaSimple<>();
        l.insertarFinal("a");
        l.insertarFinal("b");
        l.insertarFinal("c");
        assertEquals("a", l.obtener(0));
        assertEquals("b", l.obtener(1));
        assertEquals("c", l.obtener(2));
        assertEquals(3, l.tamano());
    }

    @Test
    void insertarInicioInvierteOrden() {
        ListaSimple<String> l = new ListaSimple<>();
        l.insertarInicio("a");
        l.insertarInicio("b");
        assertEquals("b", l.obtener(0));
        assertEquals("a", l.obtener(1));
        assertEquals("b", l.primero());
    }

    @Test
    void eliminarInicio() {
        ListaSimple<String> l = new ListaSimple<>();
        l.insertarFinal("a");
        l.insertarFinal("b");
        assertEquals("a", l.eliminarInicio());
        assertEquals(1, l.tamano());
        assertEquals("b", l.primero());
    }

    @Test
    void eliminarPorValorEnDistintasPosiciones() {
        ListaSimple<String> l = new ListaSimple<>();
        l.insertarFinal("a");
        l.insertarFinal("b");
        l.insertarFinal("c");

        assertTrue(l.eliminar("b")); // medio
        assertEquals(2, l.tamano());
        assertTrue(l.eliminar("a")); // cabeza
        assertEquals(1, l.tamano());
        assertEquals("c", l.primero());
        assertFalse(l.eliminar("z")); // inexistente
    }

    @Test
    void contieneYBuscarPorCriterio() {
        ListaSimple<Integer> l = new ListaSimple<>();
        l.insertarFinal(5);
        l.insertarFinal(12);
        l.insertarFinal(7);
        assertTrue(l.contiene(12));
        assertFalse(l.contiene(99));
        assertEquals(12, l.buscar(n -> n > 10));
        assertNull(l.buscar(n -> n > 100));
    }

    @Test
    void filtrarDevuelveNuevaLista() {
        ListaSimple<Integer> l = new ListaSimple<>();
        for (int i = 1; i <= 5; i++) {
            l.insertarFinal(i);
        }
        ListaSimple<Integer> impares = l.filtrar(n -> n % 2 == 1);
        assertEquals(3, impares.tamano());
        assertEquals(1, impares.obtener(0));
        assertEquals(3, impares.obtener(1));
        assertEquals(5, impares.obtener(2));
    }

    @Test
    void recorridoConIterador() {
        ListaSimple<Integer> l = new ListaSimple<>();
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
    void eliminarInicioVaciaLanzaExcepcion() {
        ListaSimple<String> l = new ListaSimple<>();
        assertThrows(NoSuchElementException.class, l::eliminarInicio);
        assertThrows(NoSuchElementException.class, l::primero);
    }

    @Test
    void limpiarDejaVacia() {
        ListaSimple<String> l = new ListaSimple<>();
        l.insertarFinal("a");
        l.limpiar();
        assertTrue(l.estaVacia());
    }
}
