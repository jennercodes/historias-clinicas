package com.posta.ui.crud;

import com.posta.model.Especialidad;
import com.posta.model.Medico;
import com.posta.ui.Contexto;
import com.posta.util.UiUtil;
import com.posta.util.ValidadorDni;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Gestion de medicos (RF04). Panel incrustado en el dashboard.
public class MedicosPanel extends JPanel {

    private final transient Contexto contexto;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Id", "DNI", "Nombres", "Apellidos", "Colegiatura", "Telefono", "Especialidad"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtNombres = new JTextField(18);
    private final JTextField txtApellidos = new JTextField(18);
    private final JTextField txtColegiatura = new JTextField(12);
    private final JTextField txtTelefono = new JTextField(12);
    private final JComboBox<Especialidad> cboEspecialidad = new JComboBox<>();
    private int idSeleccionado = 0;

    public MedicosPanel(Contexto contexto) {
        this.contexto = contexto;
        construir();
        cargarEspecialidades();
        cargarTabla();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(UiUtil.titulo("Gestion de Medicos"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del medico"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        int y = 0;
        agregar(form, g, 0, y, new JLabel("DNI:")); agregar(form, g, 1, y, txtDni);
        agregar(form, g, 2, y, new JLabel("Colegiatura:")); agregar(form, g, 3, y++, txtColegiatura);
        agregar(form, g, 0, y, new JLabel("Nombres:")); agregar(form, g, 1, y, txtNombres);
        agregar(form, g, 2, y, new JLabel("Apellidos:")); agregar(form, g, 3, y++, txtApellidos);
        agregar(form, g, 0, y, new JLabel("Telefono:")); agregar(form, g, 1, y, txtTelefono);
        agregar(form, g, 2, y, new JLabel("Especialidad:")); agregar(form, g, 3, y, cboEspecialidad);
        norte.add(form, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(boton("Nuevo", this::limpiar));
        acciones.add(boton("Guardar", this::guardar));
        acciones.add(boton("Eliminar", this::eliminar));
        add(acciones, BorderLayout.SOUTH);
    }

    private void agregar(JPanel panel, GridBagConstraints g, int x, int y, Component c) {
        g.gridx = x; g.gridy = y;
        panel.add(c, g);
    }

    private JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> accion.run());
        return b;
    }

    private void cargarEspecialidades() {
        cboEspecialidad.removeAllItems();
        for (Especialidad e : contexto.especialidades.listar()) {
            cboEspecialidad.addItem(e);
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Medico m : contexto.medicos.listar()) {
            Especialidad e = contexto.especialidades.buscarPorId(m.getEspecialidadId());
            modelo.addRow(new Object[]{m.getId(), m.getDni(), m.getNombres(), m.getApellidos(),
                    m.getColegiatura(), m.getTelefono(), e != null ? e.getNombre() : "—"});
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        idSeleccionado = (int) modelo.getValueAt(fila, 0);
        Medico m = contexto.medicos.buscarPorId(idSeleccionado);
        if (m == null) {
            return;
        }
        txtDni.setText(m.getDni());
        txtNombres.setText(m.getNombres());
        txtApellidos.setText(m.getApellidos());
        txtColegiatura.setText(m.getColegiatura());
        txtTelefono.setText(m.getTelefono());
        seleccionarEspecialidad(m.getEspecialidadId());
    }

    private void seleccionarEspecialidad(int especialidadId) {
        for (int i = 0; i < cboEspecialidad.getItemCount(); i++) {
            if (cboEspecialidad.getItemAt(i).getId() == especialidadId) {
                cboEspecialidad.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limpiar() {
        idSeleccionado = 0;
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtColegiatura.setText("");
        txtTelefono.setText("");
        if (cboEspecialidad.getItemCount() > 0) {
            cboEspecialidad.setSelectedIndex(0);
        }
        tabla.clearSelection();
        txtDni.requestFocus();
    }

    private void guardar() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        Especialidad especialidad = (Especialidad) cboEspecialidad.getSelectedItem();

        if (!ValidadorDni.esValido(dni)) {
            UiUtil.error(this, "El DNI debe tener 8 digitos.");
            return;
        }
        if (nombres.isEmpty() || apellidos.isEmpty()) {
            UiUtil.error(this, "Nombres y apellidos son obligatorios.");
            return;
        }
        if (especialidad == null) {
            UiUtil.error(this, "Seleccione una especialidad. Registre una primero si no hay.");
            return;
        }
        Medico existente = contexto.medicos.buscarPorDni(dni);
        if (existente != null && existente.getId() != idSeleccionado) {
            UiUtil.error(this, "Ya existe un medico con ese DNI.");
            return;
        }

        Medico m = new Medico(idSeleccionado, dni, nombres, apellidos,
                txtColegiatura.getText().trim(), txtTelefono.getText().trim(), especialidad.getId());
        contexto.medicos.guardar(m);
        cargarTabla();
        limpiar();
        UiUtil.info(this, "Medico guardado.");
    }

    private void eliminar() {
        if (idSeleccionado == 0) {
            UiUtil.error(this, "Seleccione un medico de la tabla.");
            return;
        }
        if (!UiUtil.confirmar(this, "¿Eliminar el medico seleccionado?")) {
            return;
        }
        contexto.medicos.eliminar(idSeleccionado);
        cargarTabla();
        limpiar();
    }
}
