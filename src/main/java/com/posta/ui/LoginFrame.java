package com.posta.ui;

import com.posta.model.Usuario;
import com.posta.util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

// Pantalla de acceso (RF01).
public class LoginFrame extends JFrame {

    private final transient Contexto contexto;
    private final JTextField txtUsuario = new JTextField(16);
    private final JPasswordField txtClave = new JPasswordField(16);

    public LoginFrame(Contexto contexto) {
        super("Posta Medica - Acceso");
        this.contexto = contexto;
        construir();
    }

    private void construir() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("Historias Clinicas");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(titulo, g);

        g.gridwidth = 1;
        g.gridx = 0; g.gridy = 1; panel.add(new JLabel("Usuario:"), g);
        g.gridx = 1; panel.add(txtUsuario, g);
        g.gridx = 0; g.gridy = 2; panel.add(new JLabel("Clave:"), g);
        g.gridx = 1; panel.add(txtClave, g);

        JButton btnIngresar = new JButton("Ingresar");
        g.gridx = 1; g.gridy = 3; g.anchor = GridBagConstraints.EAST;
        panel.add(btnIngresar, g);

        JLabel ayuda = new JLabel("Acceso por defecto: admin / admin123");
        ayuda.setFont(ayuda.getFont().deriveFont(Font.ITALIC, 11f));
        ayuda.setForeground(Color.GRAY);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        panel.add(ayuda, g);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnIngresar);
        btnIngresar.addActionListener(e -> intentarIngresar());

        pack();
        setLocationRelativeTo(null);
    }

    private void intentarIngresar() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());
        if (usuario.isEmpty() || clave.isEmpty()) {
            UiUtil.error(this, "Ingrese usuario y clave.");
            return;
        }
        Optional<Usuario> autenticado = contexto.auth.login(usuario, clave);
        if (autenticado.isEmpty()) {
            UiUtil.error(this, "Usuario o clave incorrectos.");
            txtClave.setText("");
            return;
        }
        new MenuPrincipalFrame(contexto, autenticado.get()).setVisible(true);
        dispose();
    }
}
