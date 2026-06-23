package com.posta.model;

import com.posta.datastructures.ListaDoble;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ModeloTest {

    @Test
    void igualdadPacientePorDni() {
        Paciente a = new Paciente(1, "12345678", "Ana", "Lopez", LocalDate.of(1990, 1, 1), Sexo.FEMENINO, "Av 1", "999");
        Paciente b = new Paciente(2, "12345678", "Otra", "Persona", LocalDate.of(1985, 5, 5), Sexo.FEMENINO, "Av 2", "888");
        Paciente c = new Paciente(3, "87654321", "Ana", "Lopez", LocalDate.of(1990, 1, 1), Sexo.FEMENINO, "Av 1", "999");
        assertEquals(a, b, "Mismo DNI => iguales aunque difieran otros campos");
        assertNotEquals(a, c, "Distinto DNI => distintos");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void igualdadMedicoPorDni() {
        Medico m1 = new Medico(1, "11111111", "Juan", "Perez", "CMP123", "999", 1);
        Medico m2 = new Medico(99, "11111111", "Juan", "Perez", "CMP123", "999", 2);
        assertEquals(m1, m2);
    }

    @Test
    void edadSeCalculaDesdeFechaNacimiento() {
        Paciente p = new Paciente();
        p.setFechaNacimiento(LocalDate.now().minusYears(30).minusDays(1));
        assertEquals(30, p.getEdad());
    }

    @Test
    void historiaClinicaAcumulaAtencionesEnOrden() {
        HistoriaClinica hc = new HistoriaClinica(1, 100, LocalDate.now());
        hc.agregarAtencion(new Atencion(1, LocalDateTime.now(), 1, 1, "Control", "OK", "Reposo", ""));
        hc.agregarAtencion(new Atencion(2, LocalDateTime.now(), 1, 1, "Fiebre", "Gripe", "Paracetamol", ""));
        assertEquals(2, hc.cantidadAtenciones());
        assertEquals(1, hc.getAtenciones().primero().getId());
        assertEquals(2, hc.getAtenciones().ultimo().getId());
    }

    @Test
    void serializacionRoundTripConservaListaDeAtenciones() throws Exception {
        HistoriaClinica original = new HistoriaClinica(7, 100, LocalDate.of(2026, 1, 10));
        original.setAntecedentes("Hipertension");
        original.setGrupoSanguineo("O+");
        original.agregarAtencion(new Atencion(1, LocalDateTime.of(2026, 1, 10, 9, 0), 1, 2, "Dolor", "Migrania", "Analgesico", "—"));
        original.agregarAtencion(new Atencion(2, LocalDateTime.of(2026, 2, 15, 11, 30), 3, 2, "Control", "Estable", "Seguir", "—"));

        // Serializar a bytes
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(original);
        }

        // Deserializar
        HistoriaClinica copia;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            copia = (HistoriaClinica) in.readObject();
        }

        assertEquals(7, copia.getId());
        assertEquals("Hipertension", copia.getAntecedentes());
        assertEquals(2, copia.cantidadAtenciones());

        ListaDoble<Atencion> atenciones = copia.getAtenciones();
        assertEquals(1, atenciones.primero().getId());
        assertEquals(2, atenciones.ultimo().getId());
        assertEquals("Migrania", atenciones.primero().getDiagnostico());

        // La lista doble deserializada debe seguir siendo recorrible en reversa
        var it = atenciones.enReversa().iterator();
        assertEquals(2, it.next().getId());
        assertEquals(1, it.next().getId());
        assertFalse(it.hasNext());
    }

    @Test
    void enumsSeSerializan() throws Exception {
        Usuario u = new Usuario(1, "admin", "1234", "Administrador", Rol.ADMINISTRADOR);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(u);
        }
        Usuario copia;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            copia = (Usuario) in.readObject();
        }
        assertEquals(Rol.ADMINISTRADOR, copia.getRol());
        assertEquals(u, copia); // igualdad por nombre de usuario
    }
}
