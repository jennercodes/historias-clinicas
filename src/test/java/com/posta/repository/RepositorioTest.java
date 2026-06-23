package com.posta.repository;

import com.posta.model.*;
import com.posta.util.ArchivoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioTest {

    @BeforeEach
    void usarDirectorioTemporal(@TempDir Path tmp) {
        // Cada prueba escribe en su propio directorio aislado.
        ArchivoUtil.setDirectorioBase(tmp);
    }

    @Test
    void altaAsignaIdAutoincremental() {
        RepositorioPaciente repo = new RepositorioPaciente();
        Paciente p1 = repo.guardar(nuevoPaciente("12345678", "Ana", "Lopez"));
        Paciente p2 = repo.guardar(nuevoPaciente("87654321", "Luis", "Diaz"));
        assertEquals(1, p1.getId());
        assertEquals(2, p2.getId());
        assertEquals(2, repo.cantidad());
    }

    @Test
    void datosSobrevivenAlRecargar() {
        RepositorioPaciente repo = new RepositorioPaciente();
        repo.guardar(nuevoPaciente("12345678", "Ana", "Lopez"));

        // Nueva instancia: debe leer del archivo persistido
        RepositorioPaciente recargado = new RepositorioPaciente();
        assertEquals(1, recargado.cantidad());
        Paciente p = recargado.buscarPorDni("12345678");
        assertNotNull(p);
        assertEquals("Ana", p.getNombres());
    }

    @Test
    void actualizarReemplazaSinDuplicar() {
        RepositorioPaciente repo = new RepositorioPaciente();
        Paciente p = repo.guardar(nuevoPaciente("12345678", "Ana", "Lopez"));
        p.setTelefono("999111222");
        repo.guardar(p); // mismo id => reemplaza
        assertEquals(1, repo.cantidad());
        assertEquals("999111222", repo.buscarPorId(p.getId()).getTelefono());
    }

    @Test
    void eliminarQuitaYPersiste() {
        RepositorioPaciente repo = new RepositorioPaciente();
        Paciente p = repo.guardar(nuevoPaciente("12345678", "Ana", "Lopez"));
        assertTrue(repo.eliminar(p.getId()));
        assertEquals(0, repo.cantidad());
        assertEquals(0, new RepositorioPaciente().cantidad()); // persistido
        assertFalse(repo.eliminar(999));
    }

    @Test
    void busquedaPorNombreParcialIgnoraMayusculas() {
        RepositorioPaciente repo = new RepositorioPaciente();
        repo.guardar(nuevoPaciente("1", "Ana Maria", "Lopez"));
        repo.guardar(nuevoPaciente("2", "Luis", "Maravi"));
        repo.guardar(nuevoPaciente("3", "Jose", "Diaz"));
        assertEquals(2, repo.buscarPorNombre("mar").tamano()); // Maria y Maravi
    }

    @Test
    void usuarioSiembraAdministrador() {
        RepositorioUsuario repo = new RepositorioUsuario();
        Usuario admin = repo.buscarPorUsuario("admin");
        assertNotNull(admin);
        assertEquals(Rol.ADMINISTRADOR, admin.getRol());
        // Al recargar no debe duplicar la semilla
        assertEquals(1, new RepositorioUsuario().cantidad());
    }

    @Test
    void especialidadSiembraBase() {
        RepositorioEspecialidad repo = new RepositorioEspecialidad();
        assertEquals(5, repo.cantidad());
        assertNotNull(repo.buscarPorNombre("Pediatria"));
    }

    @Test
    void historiaConAtencionesSobreviveAlRecargar() {
        RepositorioHistoria repo = new RepositorioHistoria();
        HistoriaClinica hc = new HistoriaClinica(0, 100, LocalDate.of(2026, 1, 10));
        hc.setAntecedentes("Hipertension");
        hc.agregarAtencion(new Atencion(1, LocalDateTime.of(2026, 1, 10, 9, 0), 1, 2, "Dolor", "Migrania", "Analgesico", "—"));
        hc.agregarAtencion(new Atencion(2, LocalDateTime.of(2026, 2, 15, 11, 30), 3, 2, "Control", "Estable", "Seguir", "—"));
        repo.guardar(hc);
        assertEquals(1, hc.getId());

        RepositorioHistoria recargado = new RepositorioHistoria();
        HistoriaClinica leida = recargado.buscarPorPacienteId(100);
        assertNotNull(leida);
        assertEquals("Hipertension", leida.getAntecedentes());
        assertEquals(2, leida.cantidadAtenciones());
        assertEquals("Migrania", leida.getAtenciones().primero().getDiagnostico());
        assertEquals(2, leida.getAtenciones().ultimo().getId());
    }

    @Test
    void agregarAtencionAHistoriaExistenteSePersisteConGuardarCambios() {
        RepositorioHistoria repo = new RepositorioHistoria();
        HistoriaClinica hc = repo.guardar(new HistoriaClinica(0, 200, LocalDate.now()));
        hc.agregarAtencion(new Atencion(1, LocalDateTime.now(), 1, 1, "Consulta", "OK", "Reposo", ""));
        repo.guardarCambios();

        assertEquals(1, new RepositorioHistoria().buscarPorPacienteId(200).cantidadAtenciones());
    }

    private Paciente nuevoPaciente(String dni, String nombres, String apellidos) {
        return new Paciente(0, dni, nombres, apellidos, LocalDate.of(1990, 1, 1), Sexo.FEMENINO, "Av. Siempre Viva", "999");
    }
}
