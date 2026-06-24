package com.posta.ui;

import com.posta.model.Usuario;
import com.posta.ui.crud.EspecialidadesPanel;
import com.posta.ui.crud.MedicosPanel;
import com.posta.ui.crud.PacientesPanel;
import com.posta.util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Dashboard principal (RF02). Se abre maximizado y muestra los modulos dentro
// de un area central que cambia segun la opcion elegida en el menu lateral, en
// lugar de abrir una ventana emergente por cada seccion.
public class MenuPrincipalFrame extends JFrame {

    private static final Color COLOR_BARRA = new Color(0x2C3E50);
    private static final Color COLOR_CABECERA = new Color(0x22303F);
    private static final Color COLOR_TEXTO_CLARO = new Color(0xECF0F1);
    private static final int ANCHO_LATERAL = 230;

    private final transient Contexto contexto;
    private final transient Usuario usuario;
    private final JPanel contenido = new JPanel(new BorderLayout());
    private final transient List<BotonNavegacion> navegacion = new ArrayList<>();

    public MenuPrincipalFrame(Contexto contexto, Usuario usuario) {
        super("Posta Medica - Historias Clinicas");
        this.contexto = contexto;
        this.usuario = usuario;
        construir();
    }

    private void construir() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearLateral(), BorderLayout.WEST);

        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(contenido, BorderLayout.CENTER);

        setSize(1100, 700);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH); // abrir maximizado

        // Seleccion inicial: Inicio
        navegacion.get(0).setActivo(true);
        mostrar(new InicioPanel(contexto, usuario));
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(COLOR_CABECERA);
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel titulo = new JLabel("Posta Medica");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        cabecera.add(titulo, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        derecha.setOpaque(false);
        JLabel quien = new JLabel(usuario.getNombreCompleto() + "  (" + usuario.getRol() + ")");
        quien.setForeground(COLOR_TEXTO_CLARO);
        JButton cerrar = new JButton("Cerrar sesion");
        cerrar.addActionListener(e -> cerrarSesion());
        derecha.add(quien);
        derecha.add(cerrar);
        cabecera.add(derecha, BorderLayout.EAST);

        return cabecera;
    }

    private JComponent crearLateral() {
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBackground(COLOR_BARRA);
        lateral.setPreferredSize(new Dimension(ANCHO_LATERAL, 0));

        JLabel encabezado = new JLabel("NAVEGACION");
        encabezado.setForeground(new Color(0x7F8C9B));
        encabezado.setFont(encabezado.getFont().deriveFont(Font.BOLD, 11f));
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 22, 8, 22));
        encabezado.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(encabezado);

        agregarItem(lateral, "Inicio", () -> mostrar(new InicioPanel(contexto, usuario)));
        agregarItem(lateral, "Pacientes", () -> mostrar(new PacientesPanel(contexto)));
        agregarItem(lateral, "Medicos", () -> mostrar(new MedicosPanel(contexto)));
        agregarItem(lateral, "Especialidades", () -> mostrar(new EspecialidadesPanel(contexto)));
        agregarItem(lateral, "Buscar paciente", () -> mostrar(new BuscarPacientePanel(contexto)));
        agregarItemPendiente(lateral, "Atenciones e historia");
        agregarItemPendiente(lateral, "Reportes");

        return lateral;
    }

    private void agregarItem(JPanel lateral, String texto, Runnable accion) {
        BotonNavegacion boton = new BotonNavegacion(texto);
        boton.setAccion(() -> {
            seleccionar(boton);
            accion.run();
        });
        navegacion.add(boton);
        lateral.add(boton);
    }

    // Item de un modulo aun no implementado: muestra un aviso sin cambiar la
    // seccion activa.
    private void agregarItemPendiente(JPanel lateral, String texto) {
        BotonNavegacion boton = new BotonNavegacion(texto);
        boton.setAccion(this::pendiente);
        navegacion.add(boton);
        lateral.add(boton);
    }

    private void seleccionar(BotonNavegacion activo) {
        for (BotonNavegacion b : navegacion) {
            b.setActivo(b == activo);
        }
    }

    // Reemplaza el contenido central por el panel indicado.
    private void mostrar(JComponent panel) {
        contenido.removeAll();
        contenido.add(panel, BorderLayout.CENTER);
        contenido.revalidate();
        contenido.repaint();
    }

    private void pendiente() {
        UiUtil.info(this, "Modulo disponible en la siguiente entrega de la Fase 5.");
    }

    private void cerrarSesion() {
        new LoginFrame(contexto).setVisible(true);
        dispose();
    }
}
