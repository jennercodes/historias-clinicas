package com.posta.ui;

import com.posta.model.Paciente;
import com.posta.util.ArchivoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ContextoTest {

    @Test
    void siembraDatosDemoEnElPrimerArranque(@TempDir Path tmp) {
        ArchivoUtil.setDirectorioBase(tmp);
        Contexto c = new Contexto();

        assertEquals(6, c.medicos.cantidad());
        assertEquals(8, c.pacientes.cantidad());
        assertEquals(8, c.historias.cantidad());

        Paciente ana = c.pacientes.buscarPorDni("12345678");
        assertNotNull(ana);
        assertEquals(2, c.historias.buscarPorPacienteId(ana.getId()).cantidadAtenciones());

        // El acceso por defecto debe seguir disponible.
        assertTrue(c.auth.login("admin", "admin123").isPresent());
    }

    @Test
    void noDuplicaLaDemoAlVolverAConstruir(@TempDir Path tmp) {
        ArchivoUtil.setDirectorioBase(tmp);
        new Contexto();              // primer arranque: siembra
        Contexto segundo = new Contexto(); // relee de disco, no vuelve a sembrar

        assertEquals(6, segundo.medicos.cantidad());
        assertEquals(8, segundo.pacientes.cantidad());
        assertEquals(8, segundo.historias.cantidad());
    }
}
