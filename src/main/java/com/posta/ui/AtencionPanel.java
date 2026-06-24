package com.posta.ui;

import com.posta.model.Atencion;
import com.posta.model.Especialidad;
import com.posta.model.HistoriaClinica;
import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.util.FechaUtil;
import com.posta.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

// Historia clinica de un paciente y registro de atenciones (RF06).
public class AtencionPanel extends JPanel {

    private final transient Contexto contexto;

    private final JComboBox<Paciente> cboPaciente = new JComboBox<>();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Id", "Fecha", "Medico", "Especialidad", "Motivo", "Diagnostico"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JComboBox<Medico> cboMedico = new JComboBox<>();
    private final JLabel lblEspecialidad = new JLabel("—");
    private final JTextField txtFecha = new JTextField(10);
    private final JTextField txtMotivo = new JTextField(24);
    private final JTextField txtDiagnostico = new JTextField(24);
    private final JTextField txtTratamiento = new JTextField(24);
    private final JTextField txtObservaciones = new JTextField(24);

    public AtencionPanel(Contexto contexto) {
        this.contexto = contexto;
        construir();
        cargarPacientes();
        cargarMedicos();
        cargarHistoria();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(UiUtil.titulo("Atenciones e Historia Clinica"), BorderLayout.NORTH);
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selector.add(new JLabel("Paciente:"));
        cboPaciente.setPreferredSize(new Dimension(280, cboPaciente.getPreferredSize().height));
        cboPaciente.addActionListener(e -> cargarHistoria());
        selector.add(cboPaciente);
        norte.add(selector, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial de atenciones (mas reciente primero)"));
        add(scroll, BorderLayout.CENTER);

        add(crearFormularioAtencion(), BorderLayout.SOUTH);
    }

    private JPanel crearFormularioAtencion() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Nueva atencion"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        cboMedico.addActionListener(e -> actualizarEspecialidad());
        txtFecha.setText(FechaUtil.formatear(LocalDate.now()));

        int y = 0;
        agregar(form, g, 0, y, new JLabel("Medico:")); agregar(form, g, 1, y, cboMedico);
        agregar(form, g, 2, y, new JLabel("Especialidad:")); agregar(form, g, 3, y++, lblEspecialidad);
        agregar(form, g, 0, y, new JLabel("Fecha (dd/MM/yyyy):")); agregar(form, g, 1, y++, txtFecha);
        agregar(form, g, 0, y, new JLabel("Motivo:")); agregar(form, g, 1, y, txtMotivo);
        agregar(form, g, 2, y, new JLabel("Diagnostico:")); agregar(form, g, 3, y++, txtDiagnostico);
        agregar(form, g, 0, y, new JLabel("Tratamiento:")); agregar(form, g, 1, y, txtTratamiento);
        agregar(form, g, 2, y, new JLabel("Observaciones:")); agregar(form, g, 3, y++, txtObservaciones);

        JButton btnRegistrar = new JButton("Registrar atencion");
        btnRegistrar.addActionListener(e -> registrar());
        g.gridx = 3; g.gridy = y; g.anchor = GridBagConstraints.EAST;
        form.add(btnRegistrar, g);

        return form;
    }

    private void agregar(JPanel panel, GridBagConstraints g, int x, int y, Component c) {
        g.gridx = x; g.gridy = y;
        panel.add(c, g);
    }

    private void cargarPacientes() {
        cboPaciente.removeAllItems();
        for (Paciente p : contexto.pacientes.listar()) {
            cboPaciente.addItem(p);
        }
    }

    private void cargarMedicos() {
        cboMedico.removeAllItems();
        for (Medico m : contexto.medicos.listar()) {
            cboMedico.addItem(m);
        }
        actualizarEspecialidad();
    }

    private void actualizarEspecialidad() {
        Medico m = (Medico) cboMedico.getSelectedItem();
        if (m == null) {
            lblEspecialidad.setText("—");
            return;
        }
        Especialidad e = contexto.especialidades.buscarPorId(m.getEspecialidadId());
        lblEspecialidad.setText(e != null ? e.getNombre() : "—");
    }

    private void cargarHistoria() {
        modelo.setRowCount(0);
        Paciente p = (Paciente) cboPaciente.getSelectedItem();
        if (p == null) {
            return;
        }
        HistoriaClinica hc = contexto.historias.buscarPorPacienteId(p.getId());
        if (hc == null) {
            return;
        }
        // Recorrido en reversa: las atenciones mas recientes aparecen primero.
        for (Atencion a : hc.getAtenciones().enReversa()) {
            Medico m = contexto.medicos.buscarPorId(a.getMedicoId());
            Especialidad e = contexto.especialidades.buscarPorId(a.getEspecialidadId());
            modelo.addRow(new Object[]{a.getId(), FechaUtil.formatear(a.getFechaHora()),
                    m != null ? m.getNombreCompleto() : "—", e != null ? e.getNombre() : "—",
                    a.getMotivo(), a.getDiagnostico()});
        }
    }

    private void registrar() {
        Paciente p = (Paciente) cboPaciente.getSelectedItem();
        Medico m = (Medico) cboMedico.getSelectedItem();
        if (p == null) {
            UiUtil.error(this, "Seleccione un paciente. Registre pacientes primero si no hay.");
            return;
        }
        if (m == null) {
            UiUtil.error(this, "Seleccione un medico. Registre medicos primero si no hay.");
            return;
        }
        if (txtMotivo.getText().trim().isEmpty()) {
            UiUtil.error(this, "El motivo de la atencion es obligatorio.");
            return;
        }
        LocalDate fecha = FechaUtil.parsear(txtFecha.getText());
        if (fecha == null) {
            UiUtil.error(this, "La fecha no es valida (use dd/MM/yyyy).");
            return;
        }

        contexto.atenciones.registrar(p.getId(), m.getId(), m.getEspecialidadId(),
                fecha.atTime(LocalTime.now().withNano(0)),
                txtMotivo.getText().trim(), txtDiagnostico.getText().trim(),
                txtTratamiento.getText().trim(), txtObservaciones.getText().trim());

        cargarHistoria();
        limpiarFormulario();
        UiUtil.info(this, "Atencion registrada.");
    }

    private void limpiarFormulario() {
        txtFecha.setText(FechaUtil.formatear(LocalDate.now()));
        txtMotivo.setText("");
        txtDiagnostico.setText("");
        txtTratamiento.setText("");
        txtObservaciones.setText("");
    }
}
