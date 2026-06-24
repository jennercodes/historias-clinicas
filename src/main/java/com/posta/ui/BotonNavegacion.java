package com.posta.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Item del menu lateral, pintado a mano sobre un JLabel para controlar color,
// hover y estado activo independiente del look & feel del sistema.
public class BotonNavegacion extends JLabel {

    public static final Color FONDO = new Color(0x2C3E50);
    private static final Color HOVER = new Color(0x3D566E);
    private static final Color ACTIVO = new Color(0x1ABC9C);
    private static final Color TEXTO = new Color(0xECF0F1);

    private boolean activo;
    private transient Runnable accion = () -> {};

    public BotonNavegacion(String texto) {
        super(texto);
        setOpaque(true);
        setBackground(FONDO);
        setForeground(TEXTO);
        setFont(getFont().deriveFont(Font.PLAIN, 14f));
        setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!activo) setBackground(HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!activo) setBackground(FONDO);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });
    }

    public void setAccion(Runnable accion) {
        this.accion = accion;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        setBackground(activo ? ACTIVO : FONDO);
        setForeground(activo ? Color.WHITE : TEXTO);
        // Barra de acento a la izquierda cuando esta activo.
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, activo ? 4 : 0, 0, 0,
                        activo ? Color.WHITE : FONDO),
                BorderFactory.createEmptyBorder(12, activo ? 18 : 22, 12, 22)));
    }
}
