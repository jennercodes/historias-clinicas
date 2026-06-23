package com.posta.datastructures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArregloDinamicoTest {

    @Test
    void nuevoArregloEstaVacio() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        assertTrue(a.estaVacio());
        assertEquals(0, a.tamano());
    }

    @Test
    void agregarYObtener() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("uno");
        a.agregar("dos");
        assertEquals(2, a.tamano());
        assertEquals("uno", a.obtener(0));
        assertEquals("dos", a.obtener(1));
    }

    @Test
    void redimensionaAlSuperarCapacidadInicial() {
        ArregloDinamico<Integer> a = new ArregloDinamico<>(2);
        for (int i = 0; i < 100; i++) {
            a.agregar(i);
        }
        assertEquals(100, a.tamano());
        assertEquals(0, a.obtener(0));
        assertEquals(99, a.obtener(99));
    }

    @Test
    void insertarEnPosicionDesplaza() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("a");
        a.agregar("c");
        a.insertar(1, "b");
        assertEquals("a", a.obtener(0));
        assertEquals("b", a.obtener(1));
        assertEquals("c", a.obtener(2));
        assertEquals(3, a.tamano());
    }

    @Test
    void actualizarReemplazaYDevuelveAnterior() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("viejo");
        String previo = a.actualizar(0, "nuevo");
        assertEquals("viejo", previo);
        assertEquals("nuevo", a.obtener(0));
    }

    @Test
    void eliminarPorIndiceCompacta() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("a");
        a.agregar("b");
        a.agregar("c");
        String eliminado = a.eliminar(1);
        assertEquals("b", eliminado);
        assertEquals(2, a.tamano());
        assertEquals("a", a.obtener(0));
        assertEquals("c", a.obtener(1));
    }

    @Test
    void eliminarElementoPorValor() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("x");
        a.agregar("y");
        assertTrue(a.eliminarElemento("x"));
        assertFalse(a.eliminarElemento("z"));
        assertEquals(1, a.tamano());
        assertEquals("y", a.obtener(0));
    }

    @Test
    void contieneEIndiceDeUsanEquals() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("hola");
        assertTrue(a.contiene("hola"));
        assertFalse(a.contiene("chau"));
        assertEquals(0, a.indiceDe("hola"));
        assertEquals(-1, a.indiceDe("chau"));
    }

    @Test
    void buscarPorCriterio() {
        ArregloDinamico<Integer> a = new ArregloDinamico<>();
        a.agregar(3);
        a.agregar(8);
        a.agregar(15);
        Integer encontrado = a.buscar(n -> n > 10);
        assertEquals(15, encontrado);
        assertNull(a.buscar(n -> n > 100));
    }

    @Test
    void filtrarDevuelveCoincidencias() {
        ArregloDinamico<Integer> a = new ArregloDinamico<>();
        for (int i = 1; i <= 6; i++) {
            a.agregar(i);
        }
        ArregloDinamico<Integer> pares = a.filtrar(n -> n % 2 == 0);
        assertEquals(3, pares.tamano());
        assertEquals(2, pares.obtener(0));
        assertEquals(4, pares.obtener(1));
        assertEquals(6, pares.obtener(2));
    }

    @Test
    void clonarEsCopiaIndependienteEnEstructura() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("a");
        a.agregar("b");
        ArregloDinamico<String> copia = a.clonar();
        copia.agregar("c");
        assertEquals(2, a.tamano());
        assertEquals(3, copia.tamano());
    }

    @Test
    void recorridoConIterador() {
        ArregloDinamico<Integer> a = new ArregloDinamico<>();
        a.agregar(10);
        a.agregar(20);
        a.agregar(30);
        List<Integer> vistos = new ArrayList<>();
        for (int v : a) {
            vistos.add(v);
        }
        assertEquals(List.of(10, 20, 30), vistos);
    }

    @Test
    void indiceFueraDeRangoLanzaExcepcion() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("a");
        assertThrows(IndexOutOfBoundsException.class, () -> a.obtener(5));
        assertThrows(IndexOutOfBoundsException.class, () -> a.eliminar(-1));
    }

    @Test
    void limpiarDejaVacio() {
        ArregloDinamico<String> a = new ArregloDinamico<>();
        a.agregar("a");
        a.agregar("b");
        a.limpiar();
        assertTrue(a.estaVacio());
        assertEquals(0, a.tamano());
    }
}
