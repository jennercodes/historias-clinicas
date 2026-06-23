package com.posta.ui;

import com.posta.model.Usuario;
import com.posta.ui.crud.EspecialidadesFrame;
import com.posta.ui.crud.MedicosFrame;
import com.posta.ui.crud.PacientesFrame;
import com.posta.util.UiUtil;

import javax.swing.*;
import java.awt.*;

// Menu principal con navegacion a los modulos del sistema (RF02).
public class MenuPrincipalFrame extends JFrame {

    private final transient Contexto contexto;
    private final transient Usuario usuario;

    public MenuPrincipalFrame(Contexto contexto, Usuario usuario) {
        super("Posta Medica - Menu Principal");
        this.contexto = contexto;
        this.usuario = usuario;
        construir();
    }

    private void construir() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(crearBarraMenu());
        setContentPane(crearContenido());
        setSize(620, 420);
        setLocationRelativeTo(null);
    }

    private JMenuBar crearBarraMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu gestion = new JMenu("Gestion");
        gestion.add(item("Pacientes", this::abrirPacientes));
        gestion.add(item("Medicos", this::abrirMedicos));
        gestion.add(item("Especialidades", this::abrirEspecialidades));
        barra.add(gestion);

        JMenu clinico = new JMenu("Clinico");
        clinico.add(item("Buscar paciente", this::abrirBuscarPaciente));
        clinico.add(item("Atenciones e historia", this::pendiente));
        barra.add(clinico);

        JMenu reportes = new JMenu("Reportes");
        reportes.add(item("Atenciones por medico", this::pendiente));
        reportes.add(item("Atenciones por especialidad", this::pendiente));
        barra.add(reportes);

        JMenu sesion = new JMenu("Sesion");
        sesion.add(item("Cerrar sesion", this::cerrarSesion));
        sesion.add(item("Salir", () -> System.exit(0)));
        barra.add(sesion);

        return barra;
    }

    private JPanel crearContenido() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel bienvenida = new JLabel("Bienvenido, " + usuario.getNombreCompleto()
                + "  (" + usuario.getRol() + ")", SwingConstants.CENTER);
        bienvenida.setFont(bienvenida.getFont().deriveFont(Font.BOLD, 16f));
        bienvenida.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        panel.add(bienvenida, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(0, 2, 12, 12));
        botones.setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));
        botones.add(boton("Pacientes", this::abrirPacientes));
        botones.add(boton("Medicos", this::abrirMedicos));
        botones.add(boton("Especialidades", this::abrirEspecialidades));
        botones.add(boton("Buscar paciente", this::abrirBuscarPaciente));
        botones.add(boton("Atenciones e historia", this::pendiente));
        botones.add(boton("Reportes", this::pendiente));
        panel.add(botones, BorderLayout.CENTER);

        return panel;
    }

    private JMenuItem item(String texto, Runnable accion) {
        JMenuItem mi = new JMenuItem(texto);
        mi.addActionListener(e -> accion.run());
        return mi;
    }

    private JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(180, 56));
        b.addActionListener(e -> accion.run());
        return b;
    }

    private void abrirPacientes() {
        new PacientesFrame(contexto).setVisible(true);
    }

    private void abrirMedicos() {
        new MedicosFrame(contexto).setVisible(true);
    }

    private void abrirEspecialidades() {
        new EspecialidadesFrame(contexto).setVisible(true);
    }

    private void abrirBuscarPaciente() {
        new BuscarPacienteFrame(contexto).setVisible(true);
    }

    private void pendiente() {
        UiUtil.info(this, "Modulo disponible en la siguiente entrega de la Fase 5.");
    }

    private void cerrarSesion() {
        new LoginFrame(contexto).setVisible(true);
        dispose();
    }
}
