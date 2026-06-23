package com.posta;

import com.posta.ui.Contexto;
import com.posta.ui.LoginFrame;

import javax.swing.*;

// Punto de entrada de la aplicacion: configura el aspecto del sistema, crea el
// contexto (repositorios y servicios compartidos) y muestra la pantalla de
// acceso. La demo de consola equivalente esta en com.posta.demo.DemoConsola.
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Si falla, se usa el aspecto por defecto de Swing.
            }
            Contexto contexto = new Contexto();
            new LoginFrame(contexto).setVisible(true);
        });
    }
}
