package com.posta.ui;

import com.posta.model.Usuario;
import com.posta.ui.crud.EspecialidadesPanel;
import com.posta.ui.crud.MedicosPanel;
import com.posta.ui.crud.PacientesPanel;
import com.posta.util.UiUtil;

import javax.swing.*;
import java.awt.*;

// Dashboard principal (RF02). Se abre maximizado y muestra los modulos dentro
// de un area central que cambia segun la opcion elegida en el menu lateral, en
// lugar de abrir una ventana emergente por cada seccion.
public class MenuPrincipalFrame extends JFrame {

    private static final Color COLOR_LATERAL = new Color(0x2C3E50);
    private static final Color COLOR_LATERAL_TEXTO = Color.WHITE;

    private final transient Contexto contexto;
    private final transient Usuario usuario;
    private final JPanel contenido = new JPanel(new BorderLayout());

    public MenuPrincipalFrame(Contexto contexto, Usuario usuario) {
        super("Posta Medica - Historias Clinicas");
        this.contexto = contexto;
        this.usuario = usuario;
        construir();
        mostrar(new InicioPanel(contexto, usuario));
    }

    private void construir() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearLateral(), BorderLayout.WEST);

        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contenido, BorderLayout.CENTER);

        setSize(1100, 700);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH); // abrir maximizado
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(COLOR_LATERAL);
        cabecera.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel titulo = new JLabel("Posta Medica");
        titulo.setForeground(COLOR_LATERAL_TEXTO);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        cabecera.add(titulo, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        derecha.setOpaque(false);
        JLabel quien = new JLabel(usuario.getNombreCompleto() + " (" + usuario.getRol() + ")");
        quien.setForeground(COLOR_LATERAL_TEXTO);
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
        lateral.setBackground(COLOR_LATERAL);
        lateral.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        lateral.add(itemLateral("Inicio", () -> mostrar(new InicioPanel(contexto, usuario))));
        lateral.add(itemLateral("Pacientes", () -> mostrar(new PacientesPanel(contexto))));
        lateral.add(itemLateral("Medicos", () -> mostrar(new MedicosPanel(contexto))));
        lateral.add(itemLateral("Especialidades", () -> mostrar(new EspecialidadesPanel(contexto))));
        lateral.add(itemLateral("Buscar paciente", () -> mostrar(new BuscarPacientePanel(contexto))));
        lateral.add(itemLateral("Atenciones e historia", this::pendiente));
        lateral.add(itemLateral("Reportes", this::pendiente));

        return lateral;
    }

    // Boton de navegacion del menu lateral, de ancho completo.
    private JButton itemLateral(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.addActionListener(e -> accion.run());
        // pequeno margen entre botones
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 0, 2, 0),
                b.getBorder()));
        return b;
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
