package com.posta.service;

import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.model.Sexo;
import com.posta.repository.*;
import com.posta.util.ArchivoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ServicioTest {

    private RepositorioUsuario repoUsuario;
    private RepositorioEspecialidad repoEspecialidad;
    private RepositorioMedico repoMedico;
    private RepositorioPaciente repoPaciente;
    private RepositorioHistoria repoHistoria;

    private AuthService auth;
    private BusquedaService busqueda;
    private AtencionService atenciones;
    private ReporteService reportes;

    @BeforeEach
    void preparar(@TempDir Path tmp) {
        ArchivoUtil.setDirectorioBase(tmp);
        repoUsuario = new RepositorioUsuario();
        repoEspecialidad = new RepositorioEspecialidad();
        repoMedico = new RepositorioMedico();
        repoPaciente = new RepositorioPaciente();
        repoHistoria = new RepositorioHistoria();

        auth = new AuthService(repoUsuario);
        busqueda = new BusquedaService(repoPaciente);
        atenciones = new AtencionService(repoHistoria, repoPaciente);
        reportes = new ReporteService(repoHistoria);
    }

    // ---- AuthService (RF01) ----

    @Test
    void loginCorrecto() {
        assertTrue(auth.login("admin", "admin123").isPresent());
    }

    @Test
    void loginClaveIncorrecta() {
        assertTrue(auth.login("admin", "otra").isEmpty());
    }

    @Test
    void loginUsuarioInexistente() {
        assertTrue(auth.login("nadie", "x").isEmpty());
    }

    // ---- BusquedaService (RF07) ----

    @Test
    void busquedaPorDniYNombre() {
        repoPaciente.guardar(paciente("12345678", "Ana Maria", "Lopez"));
        repoPaciente.guardar(paciente("87654321", "Luis", "Maravi"));
        assertNotNull(busqueda.porDni("12345678"));
        assertNull(busqueda.porDni("00000000"));
        assertEquals(2, busqueda.porNombre("mar").tamano()); // Maria y Maravi
    }

    // ---- AtencionService (RF06, RF08) ----

    @Test
    void registrarAbreHistoriaYReusaLaMisma() {
        Paciente p = repoPaciente.guardar(paciente("12345678", "Ana", "Lopez"));
        int esp = repoEspecialidad.buscarPorNombre("Pediatria").getId();
        Medico m = repoMedico.guardar(new Medico(0, "40000001", "Lucia", "Soto", "CMP1", "9", esp));

        atenciones.registrar(p.getId(), m.getId(), esp, LocalDate.now().atTime(9, 0), "C1", "D1", "T1", "");
        atenciones.registrar(p.getId(), m.getId(), esp, LocalDate.now().atTime(10, 0), "C2", "D2", "T2", "");

        assertEquals(1, repoHistoria.cantidad(), "Misma historia para el mismo paciente");
        assertEquals(2, repoHistoria.buscarPorPacienteId(p.getId()).cantidadAtenciones());
    }

    @Test
    void idsDeAtencionSonIncrementales() {
        Paciente p = repoPaciente.guardar(paciente("12345678", "Ana", "Lopez"));
        var a1 = atenciones.registrar(p.getId(), 1, 1, LocalDate.now().atTime(9, 0), "C1", "D1", "T1", "");
        var a2 = atenciones.registrar(p.getId(), 1, 1, LocalDate.now().atTime(10, 0), "C2", "D2", "T2", "");
        assertEquals(1, a1.getId());
        assertEquals(2, a2.getId());
    }

    @Test
    void registrarConPacienteInexistenteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> atenciones.registrar(999, 1, 1, LocalDate.now().atTime(9, 0), "C", "D", "T", ""));
    }

    @Test
    void atencionesDelDiaFiltraPorMedicoYFecha() {
        Paciente p = repoPaciente.guardar(paciente("12345678", "Ana", "Lopez"));
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);
        atenciones.registrar(p.getId(), 10, 1, hoy.atTime(9, 0), "Hoy-M10", "D", "T", "");
        atenciones.registrar(p.getId(), 10, 1, ayer.atTime(9, 0), "Ayer-M10", "D", "T", "");
        atenciones.registrar(p.getId(), 20, 1, hoy.atTime(9, 0), "Hoy-M20", "D", "T", "");

        var resultado = atenciones.atencionesDelDia(10, hoy);
        assertEquals(1, resultado.tamano());
        assertEquals("Hoy-M10", resultado.obtener(0).atencion().getMotivo());
    }

    // ---- ReporteService (RF09) ----

    @Test
    void reportePorEspecialidadYRangoInclusivo() {
        Paciente p = repoPaciente.guardar(paciente("12345678", "Ana", "Lopez"));
        // especialidad 5, dentro y fuera del rango
        atenciones.registrar(p.getId(), 1, 5, LocalDate.of(2026, 1, 1).atTime(9, 0), "Borde-desde", "D", "T", "");
        atenciones.registrar(p.getId(), 1, 5, LocalDate.of(2026, 6, 15).atTime(9, 0), "Dentro", "D", "T", "");
        atenciones.registrar(p.getId(), 1, 5, LocalDate.of(2026, 12, 31).atTime(9, 0), "Borde-hasta", "D", "T", "");
        atenciones.registrar(p.getId(), 1, 5, LocalDate.of(2027, 1, 1).atTime(9, 0), "Fuera", "D", "T", "");
        // otra especialidad dentro del rango (no debe contar)
        atenciones.registrar(p.getId(), 1, 3, LocalDate.of(2026, 6, 15).atTime(9, 0), "OtraEsp", "D", "T", "");

        var resultado = reportes.porEspecialidadYRango(5, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        assertEquals(3, resultado.tamano(), "Incluye ambos bordes, excluye fuera de rango y otras especialidades");
    }

    private Paciente paciente(String dni, String nombres, String apellidos) {
        return new Paciente(0, dni, nombres, apellidos, LocalDate.of(1990, 1, 1), Sexo.FEMENINO, "Dir", "999");
    }
}
