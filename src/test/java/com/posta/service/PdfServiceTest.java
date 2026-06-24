package com.posta.service;

import com.posta.model.HistoriaClinica;
import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.model.Sexo;
import com.posta.repository.*;
import com.posta.util.ArchivoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    @TempDir
    Path tmp;

    private RepositorioEspecialidad especialidades;
    private RepositorioMedico medicos;
    private RepositorioPaciente pacientes;
    private RepositorioHistoria historias;
    private AtencionService atenciones;
    private PdfService pdf;

    @BeforeEach
    void preparar() {
        ArchivoUtil.setDirectorioBase(tmp);
        especialidades = new RepositorioEspecialidad();
        medicos = new RepositorioMedico();
        pacientes = new RepositorioPaciente();
        historias = new RepositorioHistoria();
        atenciones = new AtencionService(historias, pacientes);
        pdf = new PdfService(medicos, especialidades);
    }

    @Test
    void generaPdfValidoConAtenciones() throws Exception {
        int idEsp = especialidades.buscarPorNombre("Pediatria").getId();
        Medico m = medicos.guardar(new Medico(0, "40000001", "Lucia", "Soto", "CMP1", "9", idEsp));
        Paciente p = pacientes.guardar(new Paciente(0, "12345678", "Ana", "Lopez",
                LocalDate.of(1990, 1, 1), Sexo.FEMENINO, "Av. Lima 100", "999"));
        atenciones.registrar(p.getId(), m.getId(), idEsp, LocalDate.now().atTime(9, 0),
                "Control", "Estable", "Reposo", "—");
        HistoriaClinica hc = historias.buscarPorPacienteId(p.getId());

        Path destino = tmp.resolve("historia.pdf");
        Path resultado = pdf.generar(p, hc, destino);

        assertTrue(Files.exists(resultado));
        assertTrue(Files.size(resultado) > 0);
        assertEquals("%PDF", cabecera(resultado), "El archivo debe ser un PDF valido");
    }

    @Test
    void generaPdfAunSinAtenciones() throws Exception {
        Paciente p = pacientes.guardar(new Paciente(0, "87654321", "Luis", "Diaz",
                LocalDate.of(2000, 6, 6), Sexo.MASCULINO, "Jr. Union 50", "888"));
        HistoriaClinica hc = historias.guardar(new HistoriaClinica(0, p.getId(), LocalDate.now()));

        Path destino = tmp.resolve("sub/historia-vacia.pdf"); // tambien crea el directorio
        Path resultado = pdf.generar(p, hc, destino);

        assertTrue(Files.exists(resultado));
        assertEquals("%PDF", cabecera(resultado));
    }

    private String cabecera(Path archivo) throws Exception {
        byte[] todo = Files.readAllBytes(archivo);
        return new String(todo, 0, 4, StandardCharsets.US_ASCII);
    }
}
