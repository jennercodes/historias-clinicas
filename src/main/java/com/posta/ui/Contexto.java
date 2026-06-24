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
            medicos.guardar(new Medico(0, "40000004", "Miguel", "Torres", "CMP12348", "987777888", idEspecialidad("Odontologia")));
            medicos.guardar(new Medico(0, "40000005", "Elena", "Vargas", "CMP12349", "987999000", idEspecialidad("Cardiologia")));
            medicos.guardar(new Medico(0, "40000006", "Jorge", "Rojas", "CMP12350", "986111333", idEspecialidad("Medicina General")));
        }
        if (pacientes.cantidad() == 0) {
            pacientes.guardar(new Paciente(0, "12345678", "Ana Maria", "Lopez", LocalDate.of(1992, 5, 20), Sexo.FEMENINO, "Av. Lima 100", "900100200"));
            pacientes.guardar(new Paciente(0, "87654321", "Luis", "Maravi", LocalDate.of(2018, 3, 8), Sexo.MASCULINO, "Jr. Union 50", "900300400"));
            pacientes.guardar(new Paciente(0, "11223344", "Jose", "Diaz", LocalDate.of(1980, 11, 2), Sexo.MASCULINO, "Calle Sol 12", "900500600"));
            pacientes.guardar(new Paciente(0, "55667788", "Carmen", "Ruiz", LocalDate.of(2000, 7, 15), Sexo.FEMENINO, "Av. Mar 8", "900700800"));
            pacientes.guardar(new Paciente(0, "22334455", "Pedro", "Quispe", LocalDate.of(1975, 9, 30), Sexo.MASCULINO, "Av. Sur 45", "901111222"));
            pacientes.guardar(new Paciente(0, "33445566", "Maria", "Flores", LocalDate.of(1988, 2, 14), Sexo.FEMENINO, "Jr. Norte 7", "901333444"));
            pacientes.guardar(new Paciente(0, "44556677", "Sofia", "Castro", LocalDate.of(2015, 12, 1), Sexo.FEMENINO, "Calle Luna 3", "901555666"));
            pacientes.guardar(new Paciente(0, "66778899", "Diego", "Mendoza", LocalDate.of(1995, 6, 22), Sexo.MASCULINO, "Av. Este 90", "901777888"));
        }
        if (historias.cantidad() == 0) {
            // pacienteDni, medicoDni, dias atras, hora, motivo, diagnostico, tratamiento
            atencion("12345678", "40000001", 10, 9, "Dolor de cabeza", "Migrana", "Analgesico cada 8 horas");
            atencion("12345678", "40000001", 0, 10, "Control", "Estable", "Continuar tratamiento");
            atencion("87654321", "40000002", 5, 11, "Fiebre", "Gripe", "Paracetamol");
            atencion("87654321", "40000002", 0, 12, "Control pediatrico", "Normal", "Vacunas al dia");
            atencion("11223344", "40000006", 0, 8, "Dolor lumbar", "Lumbalgia", "Reposo y antiinflamatorio");
            atencion("55667788", "40000003", 3, 15, "Control ginecologico", "Normal", "Examen anual");
            atencion("55667788", "40000003", 0, 16, "Molestias", "Infeccion leve", "Antibiotico");
            atencion("22334455", "40000005", 20, 9, "Palpitaciones", "Arritmia leve", "Control mensual");
            atencion("22334455", "40000005", 0, 10, "Control cardiologico", "Estable", "Continuar");
            atencion("33445566", "40000004", 2, 14, "Dolor de muela", "Caries", "Empaste");
            atencion("44556677", "40000002", 0, 9, "Tos", "Resfrio comun", "Jarabe");
            atencion("66778899", "40000001", 15, 17, "Chequeo general", "Saludable", "Sin indicaciones");
        }
    }

    private void atencion(String pacienteDni, String medicoDni, int diasAtras, int hora,
                          String motivo, String diagnostico, String tratamiento) {
        Paciente p = pacientes.buscarPorDni(pacienteDni);
        Medico m = medicos.buscarPorDni(medicoDni);
        if (p != null && m != null) {
            atenciones.registrar(p.getId(), m.getId(), m.getEspecialidadId(),
                    LocalDate.now().minusDays(diasAtras).atTime(hora, 0),
                    motivo, diagnostico, tratamiento, "—");
        }
    }

    private int idEspecialidad(String nombre) {
        Especialidad e = especialidades.buscarPorNombre(nombre);
        return e != null ? e.getId() : 0;
    }
}
