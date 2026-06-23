package com.posta.ui;

import com.posta.model.Usuario;

import javax.swing.*;
import java.awt.*;

// Panel de inicio del dashboard: bienvenida y resumen de registros.
public class InicioPanel extends JPanel {

    public InicioPanel(Contexto contexto, Usuario usuario) {
        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC)),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));

        JLabel titulo = new JLabel("Posta Medica - Historias Clinicas");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bienvenida = new JLabel("Bienvenido, " + usuario.getNombreCompleto()
                + " (" + usuario.getRol() + ")");
        bienvenida.setAlignmentX(Component.LEFT_ALIGNMENT);
        bienvenida.setBorder(BorderFactory.createEmptyBorder(8, 0, 16, 0));

        JLabel resumen = new JLabel("<html>"
                + "Pacientes registrados: <b>" + contexto.pacientes.cantidad() + "</b><br>"
                + "Medicos registrados: <b>" + contexto.medicos.cantidad() + "</b><br>"
                + "Especialidades: <b>" + contexto.especialidades.cantidad() + "</b><br>"
                + "Historias clinicas: <b>" + contexto.historias.cantidad() + "</b>"
                + "</html>");
        resumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel ayuda = new JLabel("Use el menu lateral para navegar entre los modulos.");
        ayuda.setForeground(Color.GRAY);
        ayuda.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(titulo);
        tarjeta.add(bienvenida);
        tarjeta.add(resumen);
        tarjeta.add(ayuda);

        add(tarjeta);
    }
}
