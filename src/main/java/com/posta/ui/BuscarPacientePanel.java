package com.posta.ui;

import com.posta.model.Paciente;
import com.posta.util.FechaUtil;
import com.posta.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Busqueda de pacientes por DNI o nombre (RF07).
public class BuscarPacientePanel extends JPanel {

    private final transient Contexto contexto;

    private final JTextField txtCriterio = new JTextField(20);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Id", "DNI", "Nombres", "Apellidos", "F. Nac.", "Sexo", "Edad"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public BuscarPacientePanel(Contexto contexto) {
        this.contexto = contexto;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(UiUtil.titulo("Buscar Paciente"), BorderLayout.NORTH);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(new JLabel("Criterio:"));
        barra.add(txtCriterio);
        JButton btnDni = new JButton("Buscar por DNI");
        JButton btnNombre = new JButton("Buscar por nombre");
        btnDni.addActionListener(e -> buscarPorDni());
        btnNombre.addActionListener(e -> buscarPorNombre());
        barra.add(btnDni);
        barra.add(btnNombre);
        norte.add(barra, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void buscarPorDni() {
        String dni = txtCriterio.getText().trim();
        modelo.setRowCount(0);
        Paciente p = contexto.busqueda.porDni(dni);
        if (p == null) {
            UiUtil.info(this, "No se encontro un paciente con DNI " + dni + ".");
            return;
        }
        agregarFila(p);
    }

    private void buscarPorNombre() {
        modelo.setRowCount(0);
        int encontrados = 0;
        for (Paciente p : contexto.busqueda.porNombre(txtCriterio.getText().trim())) {
            agregarFila(p);
            encontrados++;
        }
        if (encontrados == 0) {
            UiUtil.info(this, "Sin coincidencias.");
        }
    }

    private void agregarFila(Paciente p) {
        modelo.addRow(new Object[]{p.getId(), p.getDni(), p.getNombres(), p.getApellidos(),
                FechaUtil.formatear(p.getFechaNacimiento()), p.getSexo(), p.getEdad()});
    }
}
