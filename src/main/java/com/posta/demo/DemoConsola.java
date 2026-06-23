package com.posta.demo;

import com.posta.datastructures.ArregloDinamico;
import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.model.Sexo;
import com.posta.repository.*;
import com.posta.service.*;
import com.posta.util.ArchivoUtil;

import java.nio.file.Files;
import java.time.LocalDate;

// Demo de consola que ejercita la logica de negocio (Fase 4) de extremo a
// extremo: login, alta de pacientes, registro de atenciones y reportes.
// La aplicacion real se lanza desde com.posta.Main (interfaz Swing).
public class DemoConsola {

    public static void main(String[] args) throws Exception {
        // Directorio temporal aislado para que la demo no escriba en ./data
        ArchivoUtil.setDirectorioBase(Files.createTempDirectory("posta-demo"));

        RepositorioUsuario repoUsuario = new RepositorioUsuario();
        RepositorioEspecialidad repoEspecialidad = new RepositorioEspecialidad();
        RepositorioMedico repoMedico = new RepositorioMedico();
        RepositorioPaciente repoPaciente = new RepositorioPaciente();
        RepositorioHistoria repoHistoria = new RepositorioHistoria();

        AuthService auth = new AuthService(repoUsuario);
        BusquedaService busqueda = new BusquedaService(repoPaciente);
        AtencionService atenciones = new AtencionService(repoHistoria, repoPaciente);
        ReporteService reportes = new ReporteService(repoHistoria);

        titulo("DEMO DE CONSOLA - LOGICA DE NEGOCIO (Fase 4)");

        // 1. Login (RF01)
        titulo("1. Autenticacion (RF01)");
        System.out.println("  admin / clave incorrecta -> " +
                auth.login("admin", "xxx").isPresent());
        System.out.println("  admin / admin123        -> " +
                auth.login("admin", "admin123").map(u -> "OK: " + u.getNombreCompleto()).orElse("FALLO"));

        // Datos de apoyo: medicos en especialidades sembradas
        int idMedicinaGeneral = repoEspecialidad.buscarPorNombre("Medicina General").getId();
        int idPediatria = repoEspecialidad.buscarPorNombre("Pediatria").getId();
        Medico drPerez = repoMedico.guardar(new Medico(0, "40000001", "Carlos", "Perez", "CMP111", "999", idMedicinaGeneral));
        Medico draSoto = repoMedico.guardar(new Medico(0, "40000002", "Lucia", "Soto", "CMP222", "988", idPediatria));

        // 2. Alta de pacientes
        titulo("2. Registro de pacientes (RF03)");
        Paciente ana = repoPaciente.guardar(new Paciente(0, "12345678", "Ana Maria", "Lopez",
                LocalDate.of(1992, 5, 20), Sexo.FEMENINO, "Av. Lima 100", "900100200"));
        Paciente luis = repoPaciente.guardar(new Paciente(0, "87654321", "Luis", "Maravi",
                LocalDate.of(2018, 3, 8), Sexo.MASCULINO, "Jr. Union 50", "900300400"));
        System.out.println("  Registrados: " + ana + " | " + luis);

        // 3. Busqueda (RF07)
        titulo("3. Busqueda de pacientes (RF07)");
        System.out.println("  Por DNI 12345678  -> " + busqueda.porDni("12345678"));
        System.out.print("  Por nombre 'mar'  -> ");
        imprimirPacientes(busqueda.porNombre("mar"));

        // 4. Registro de atenciones (RF06)
        titulo("4. Registro de atenciones (RF06)");
        LocalDate hoy = LocalDate.now();
        atenciones.registrar(ana.getId(), drPerez.getId(), idMedicinaGeneral,
                hoy.atTime(9, 0), "Dolor de cabeza", "Migrania", "Analgesico", "Reposo");
        atenciones.registrar(ana.getId(), draSoto.getId(), idPediatria,
                hoy.atTime(10, 30), "Control", "Estable", "Continuar", "—");
        atenciones.registrar(luis.getId(), draSoto.getId(), idPediatria,
                LocalDate.of(2026, 1, 15).atTime(11, 0), "Fiebre", "Gripe", "Paracetamol", "—");
        System.out.println("  Historias abiertas: " + repoHistoria.cantidad());
        System.out.println("  Atenciones de Ana:  " + repoHistoria.buscarPorPacienteId(ana.getId()).cantidadAtenciones());

        // 5. Atenciones del dia por medico (RF08)
        titulo("5. Atenciones de la Dra. Soto hoy (RF08)");
        imprimirRegistros(atenciones.atencionesDelDia(draSoto.getId(), hoy), repoPaciente);

        // 6. Reporte por especialidad y rango (RF09)
        titulo("6. Atenciones de Pediatria en 2026 (RF09)");
        imprimirRegistros(
                reportes.porEspecialidadYRango(idPediatria, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)),
                repoPaciente);

        System.out.println("\nDemo finalizada correctamente.");
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }

    private static void imprimirPacientes(ArregloDinamico<Paciente> pacientes) {
        if (pacientes.estaVacio()) {
            System.out.println("(sin resultados)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Paciente p : pacientes) {
            sb.append(p.getNombreCompleto()).append("; ");
        }
        System.out.println(sb);
    }

    private static void imprimirRegistros(ArregloDinamico<RegistroAtencion> registros,
                                          RepositorioPaciente repoPaciente) {
        if (registros.estaVacio()) {
            System.out.println("  (sin resultados)");
            return;
        }
        for (RegistroAtencion r : registros) {
            Paciente p = repoPaciente.buscarPorId(r.pacienteId());
            System.out.printf("  - %s | %s | %s%n",
                    r.atencion().getFechaHora(),
                    p != null ? p.getNombreCompleto() : ("paciente " + r.pacienteId()),
                    r.atencion().getMotivo());
        }
    }
}
