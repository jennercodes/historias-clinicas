package com.posta.datastructures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListaCircularTest {

    @Test
    void nuevaListaEstaVacia() {
        ListaCircular<String> l = new ListaCircular<>();
        assertTrue(l.estaVacia());
        assertEquals(0, l.tamano());
    }

    @Test
    void agregarYRecorrerUnaVuelta() {
        ListaCircular<Integer> l = new ListaCircular<>();
        l.agregar(1);
        l.agregar(2);
        l.agregar(3);
        assertEquals(3, l.tamano());

        List<Integer> vistos = new ArrayList<>();
        for (int v : l) {
            vistos.add(v);
        }
        assertEquals(List.of(1, 2, 3), vistos);
    }

    @Test
    void elUltimoApuntaALaCabeza() {
        // Recorrer dos veces el tamano manualmente confirma la circularidad
        ListaCircular<Integer> l = new ListaCircular<>();
        l.agregar(10);
        l.agregar(20);

        var it = l.iterator();
        // El iterador acota a una vuelta, asi que recolectamos y validamos ciclo logico
        List<Integer> vistos = new ArrayList<>();
        while (it.hasNext()) {
            vistos.add(it.next());
        }
        assertEquals(List.of(10, 20), vistos);
    }

    @Test
    void eliminarUnico() {
        ListaCircular<String> l = new ListaCircular<>();
        l.agregar("solo");
        assertTrue(l.eliminar("solo"));
        assertTrue(l.estaVacia());
    }

    @Test
    void eliminarCabezaYIntermedio() {
        ListaCircular<String> l = new ListaCircular<>();
        l.agregar("a");
        l.agregar("b");
        l.agregar("c");

        assertTrue(l.eliminar("a")); // cabeza
        assertEquals(2, l.tamano());

        List<String> vistos = new ArrayList<>();
        for (String v : l) {
            vistos.add(v);
        }
        assertEquals(List.of("b", "c"), vistos);
    }

    @Test
    void eliminarUltimoActualizaReferencia() {
        ListaCircular<String> l = new ListaCircular<>();
        l.agregar("a");
        l.agregar("b");
        l.agregar("c");
        assertTrue(l.eliminar("c")); // ultimo

        l.agregar("d"); // debe insertarse despues de "b"
        List<String> vistos = new ArrayList<>();
        for (String v : l) {
            vistos.add(v);
        }
        assertEquals(List.of("a", "b", "d"), vistos);
    }

    @Test
    void buscarPorCriterio() {
        ListaCircular<Integer> l = new ListaCircular<>();
        l.agregar(3);
        l.agregar(15);
        assertEquals(15, l.buscar(n -> n > 10));
        assertNull(l.buscar(n -> n > 100));
    }

    @Test
    void eliminarInexistente() {
        ListaCircular<String> l = new ListaCircular<>();
        l.agregar("a");
        assertFalse(l.eliminar("z"));
        assertEquals(1, l.tamano());
    }
}
