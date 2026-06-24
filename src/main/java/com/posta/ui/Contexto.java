package com.posta.ui;

import com.posta.model.Especialidad;
import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.model.Sexo;
import com.posta.repository.*;
import com.posta.service.*;

import java.time.LocalDate;

// Instancia unica de cada repositorio y servicio, compartida por todas las
// ventanas (misma data en memoria).
public class Contexto {

    public final RepositorioUsuario usuarios = new RepositorioUsuario();
    public final RepositorioEspecialidad especialidades = new RepositorioEspecialidad();
    public final RepositorioMedico medicos = new RepositorioMedico();
    public final RepositorioPaciente pacientes = new RepositorioPaciente();
    public final RepositorioHistoria historias = new RepositorioHistoria();

    public final AuthService auth = new AuthService(usuarios);
    public final BusquedaService busqueda = new BusquedaService(pacientes);
    public final AtencionService atenciones = new AtencionService(historias, pacientes);
    public final ReporteService reportes = new ReporteService(historias);
    public final PdfService pdf = new PdfService(medicos, especialidades);

    public Contexto() {
        sembrarDatosDemo();
    }

    // Carga medicos, pacientes y una historia de ejemplo la primera vez, para que
    // la aplicacion sea demostrable sin tener que ingresar datos a mano.
    private void sembrarDatosDemo() {
        if (medicos.cantidad() == 0) {
            medicos.guardar(new Medico(0, "40000001", "Carlos", "Perez", "CMP12345", "987111222", idEspecialidad("Medicina General")));
            medicos.guardar(new Medico(0, "40000002", "Lucia", "Soto", "CMP12346", "987333444", idEspecialidad("Pediatria")));
            medicos.guardar(new Medico(0, "40000003", "Rosa", "Gomez", "CMP12347", "987555666", idEspecialidad("Ginecologia")));
        }
        if (pacientes.cantidad() == 0) {
            pacientes.guardar(new Paciente(0, "12345678", "Ana Maria", "Lopez", LocalDate.of(1992, 5, 20), Sexo.FEMENINO, "Av. Lima 100", "900100200"));
            pacientes.guardar(new Paciente(0, "87654321", "Luis", "Maravi", LocalDate.of(2018, 3, 8), Sexo.MASCULINO, "Jr. Union 50", "900300400"));
            pacientes.guardar(new Paciente(0, "11223344", "Jose", "Diaz", LocalDate.of(1980, 11, 2), Sexo.MASCULINO, "Calle Sol 12", "900500600"));
            pacientes.guardar(new Paciente(0, "55667788", "Carmen", "Ruiz", LocalDate.of(2000, 7, 15), Sexo.FEMENINO, "Av. Mar 8", "900700800"));
        }
        if (historias.cantidad() == 0) {
            Paciente ana = pacientes.buscarPorDni("12345678");
            Medico drPerez = medicos.buscarPorDni("40000001");
            if (ana != null && drPerez != null) {
                atenciones.registrar(ana.getId(), drPerez.getId(), drPerez.getEspecialidadId(),
                        LocalDate.now().minusDays(10).atTime(9, 0),
                        "Dolor de cabeza", "Migrana", "Analgesico cada 8 horas", "Reposo");
                atenciones.registrar(ana.getId(), drPerez.getId(), drPerez.getEspecialidadId(),
                        LocalDate.now().atTime(10, 30),
                        "Control", "Estable", "Continuar tratamiento", "—");
            }
        }
    }

    private int idEspecialidad(String nombre) {
        Especialidad e = especialidades.buscarPorNombre(nombre);
        return e != null ? e.getId() : 0;
    }
}
