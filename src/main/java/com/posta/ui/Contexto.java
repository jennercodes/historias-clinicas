package com.posta.ui;

import com.posta.repository.*;
import com.posta.service.*;

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
}
