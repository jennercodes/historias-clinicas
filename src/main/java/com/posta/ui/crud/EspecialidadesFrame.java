package com.posta.ui.crud;

import com.posta.model.Especialidad;
import com.posta.ui.Contexto;
import com.posta.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Gestion de especialidades medicas (RF05).
public class EspecialidadesFrame extends JFrame {

    private final transient Contexto contexto;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Id", "Nombre", "Descripcion"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtDescripcion = new JTextField(20);
    private int idSeleccionado = 0;

    public EspecialidadesFrame(Contexto contexto) {
        super("Gestion de Especialidades");
        this.contexto = contexto;
        construir();
        cargarTabla();
    }

    private void construir() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos de la especialidad"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Nombre:"), g);
        g.gridx = 1; form.add(txtNombre, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Descripcion:"), g);
        g.gridx = 1; form.add(txtDescripcion, g);
        add(form, BorderLayout.NORTH);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(boton("Nuevo", this::limpiar));
        acciones.add(boton("Guardar", this::guardar));
        acciones.add(boton("Eliminar", this::eliminar));
        add(acciones, BorderLayout.SOUTH);

        setSize(640, 420);
        setLocationRelativeTo(null);
    }

    private JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> accion.run());
        return b;
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Especialidad e : contexto.especialidades.listar()) {
            modelo.addRow(new Object[]{e.getId(), e.getNombre(), e.getDescripcion()});
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        idSeleccionado = (int) modelo.getValueAt(fila, 0);
        txtNombre.setText((String) modelo.getValueAt(fila, 1));
        txtDescripcion.setText((String) modelo.getValueAt(fila, 2));
    }

    private void limpiar() {
        idSeleccionado = 0;
        txtNombre.setText("");
        txtDescripcion.setText("");
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            UiUtil.error(this, "El nombre es obligatorio.");
            return;
        }
        Especialidad e = new Especialidad(idSeleccionado, nombre, txtDescripcion.getText().trim());
        contexto.especialidades.guardar(e);
        cargarTabla();
        limpiar();
        UiUtil.info(this, "Especialidad guardada.");
    }

    private void eliminar() {
        if (idSeleccionado == 0) {
            UiUtil.error(this, "Seleccione una especialidad de la tabla.");
            return;
        }
        if (!UiUtil.confirmar(this, "¿Eliminar la especialidad seleccionada?")) {
            return;
        }
        contexto.especialidades.eliminar(idSeleccionado);
        cargarTabla();
        limpiar();
    }
}
