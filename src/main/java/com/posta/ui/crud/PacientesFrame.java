package com.posta.ui.crud;

import com.posta.model.Paciente;
import com.posta.model.Sexo;
import com.posta.ui.Contexto;
import com.posta.util.FechaUtil;
import com.posta.util.UiUtil;
import com.posta.util.ValidadorDni;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

// Gestion de pacientes (RF03) con busqueda por nombre (RF07).
public class PacientesFrame extends JFrame {

    private final transient Contexto contexto;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Id", "DNI", "Nombres", "Apellidos", "F. Nac.", "Sexo", "Direccion", "Telefono"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JTextField txtBuscar = new JTextField(18);
    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtNombres = new JTextField(18);
    private final JTextField txtApellidos = new JTextField(18);
    private final JTextField txtFechaNac = new JTextField(10);
    private final JComboBox<Sexo> cboSexo = new JComboBox<>(Sexo.values());
    private final JTextField txtDireccion = new JTextField(20);
    private final JTextField txtTelefono = new JTextField(12);
    private int idSeleccionado = 0;

    public PacientesFrame(Contexto contexto) {
        super("Gestion de Pacientes");
        this.contexto = contexto;
        construir();
        cargarTabla(contexto.pacientes.listar());
    }

    private void construir() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.NORTH);

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

        setSize(880, 520);
        setLocationRelativeTo(null);
    }

    private JPanel crearFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(4, 4));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.add(new JLabel("Buscar por nombre/apellido:"));
        busqueda.add(txtBuscar);
        busqueda.add(boton("Buscar", this::buscar));
        busqueda.add(boton("Ver todos", () -> {
            txtBuscar.setText("");
            cargarTabla(contexto.pacientes.listar());
        }));
        contenedor.add(busqueda, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        int y = 0;
        agregar(form, g, 0, y, new JLabel("DNI:")); agregar(form, g, 1, y, txtDni);
        agregar(form, g, 2, y, new JLabel("F. Nacimiento (dd/MM/yyyy):")); agregar(form, g, 3, y++, txtFechaNac);
        agregar(form, g, 0, y, new JLabel("Nombres:")); agregar(form, g, 1, y, txtNombres);
        agregar(form, g, 2, y, new JLabel("Apellidos:")); agregar(form, g, 3, y++, txtApellidos);
        agregar(form, g, 0, y, new JLabel("Sexo:")); agregar(form, g, 1, y, cboSexo);
        agregar(form, g, 2, y, new JLabel("Telefono:")); agregar(form, g, 3, y++, txtTelefono);
        agregar(form, g, 0, y, new JLabel("Direccion:")); agregar(form, g, 1, y, txtDireccion);
        contenedor.add(form, BorderLayout.CENTER);

        return contenedor;
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

    private void cargarTabla(Iterable<Paciente> pacientes) {
        modelo.setRowCount(0);
        for (Paciente p : pacientes) {
            modelo.addRow(new Object[]{p.getId(), p.getDni(), p.getNombres(), p.getApellidos(),
                    FechaUtil.formatear(p.getFechaNacimiento()), p.getSexo(),
                    p.getDireccion(), p.getTelefono()});
        }
    }

    private void buscar() {
        cargarTabla(contexto.busqueda.porNombre(txtBuscar.getText().trim()));
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        idSeleccionado = (int) modelo.getValueAt(fila, 0);
        Paciente p = contexto.pacientes.buscarPorId(idSeleccionado);
        if (p == null) {
            return;
        }
        txtDni.setText(p.getDni());
        txtNombres.setText(p.getNombres());
        txtApellidos.setText(p.getApellidos());
        txtFechaNac.setText(FechaUtil.formatear(p.getFechaNacimiento()));
        cboSexo.setSelectedItem(p.getSexo());
        txtDireccion.setText(p.getDireccion());
        txtTelefono.setText(p.getTelefono());
    }

    private void limpiar() {
        idSeleccionado = 0;
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtFechaNac.setText("");
        cboSexo.setSelectedIndex(0);
        txtDireccion.setText("");
        txtTelefono.setText("");
        tabla.clearSelection();
        txtDni.requestFocus();
    }

    private void guardar() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();

        if (!ValidadorDni.esValido(dni)) {
            UiUtil.error(this, "El DNI debe tener 8 digitos.");
            return;
        }
        if (nombres.isEmpty() || apellidos.isEmpty()) {
            UiUtil.error(this, "Nombres y apellidos son obligatorios.");
            return;
        }
        LocalDate fechaNac = FechaUtil.parsear(txtFechaNac.getText());
        if (fechaNac == null) {
            UiUtil.error(this, "La fecha de nacimiento no es valida (use dd/MM/yyyy).");
            return;
        }
        if (fechaNac.isAfter(LocalDate.now())) {
            UiUtil.error(this, "La fecha de nacimiento no puede ser futura.");
            return;
        }
        Paciente existente = contexto.pacientes.buscarPorDni(dni);
        if (existente != null && existente.getId() != idSeleccionado) {
            UiUtil.error(this, "Ya existe un paciente con ese DNI.");
            return;
        }

        Paciente p = new Paciente(idSeleccionado, dni, nombres, apellidos, fechaNac,
                (Sexo) cboSexo.getSelectedItem(), txtDireccion.getText().trim(), txtTelefono.getText().trim());
        contexto.pacientes.guardar(p);
        cargarTabla(contexto.pacientes.listar());
        limpiar();
        UiUtil.info(this, "Paciente guardado.");
    }

    private void eliminar() {
        if (idSeleccionado == 0) {
            UiUtil.error(this, "Seleccione un paciente de la tabla.");
            return;
        }
        if (!UiUtil.confirmar(this, "¿Eliminar el paciente seleccionado?")) {
            return;
        }
        contexto.pacientes.eliminar(idSeleccionado);
        cargarTabla(contexto.pacientes.listar());
        limpiar();
    }
}
