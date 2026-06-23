package com.posta.ui;

import com.posta.repository.*;
import com.posta.service.*;

// Contexto de la aplicacion: crea una unica instancia de cada repositorio y
// servicio para que todas las ventanas compartan las mismas estructuras de
// datos en memoria (y, por lo tanto, los mismos datos).
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
}
