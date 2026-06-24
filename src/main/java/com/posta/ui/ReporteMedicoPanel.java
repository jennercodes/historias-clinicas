package com.posta.ui;

import com.posta.model.Especialidad;
import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.service.RegistroAtencion;
import com.posta.util.FechaUtil;
import com.posta.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

// Reporte de atenciones realizadas por un medico en una fecha (RF08).
public class ReporteMedicoPanel extends JPanel {

    private final transient Contexto contexto;

    private final JComboBox<Medico> cboMedico = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(10);
    private final JLabel lblTotal = new JLabel(" ");
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Fecha", "Paciente", "Especialidad", "Motivo", "Diagnostico"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public ReporteMedicoPanel(Contexto contexto) {
        this.contexto = contexto;
        construir();
        cargarMedicos();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(UiUtil.titulo("Atenciones por Medico"), BorderLayout.NORTH);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.add(new JLabel("Medico:"));
        filtros.add(cboMedico);
        filtros.add(new JLabel("Fecha (dd/MM/yyyy):"));
        txtFecha.setText(FechaUtil.formatear(LocalDate.now()));
        filtros.add(txtFecha);
        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(e -> generar());
        filtros.add(btnGenerar);
        filtros.add(lblTotal);
        norte.add(filtros, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void cargarMedicos() {
        cboMedico.removeAllItems();
        for (Medico m : contexto.medicos.listar()) {
            cboMedico.addItem(m);
        }
    }

    private void generar() {
        Medico m = (Medico) cboMedico.getSelectedItem();
        if (m == null) {
            UiUtil.error(this, "Seleccione un medico.");
            return;
        }
        LocalDate fecha = FechaUtil.parsear(txtFecha.getText());
        if (fecha == null) {
            UiUtil.error(this, "La fecha no es valida (use dd/MM/yyyy).");
            return;
        }

        modelo.setRowCount(0);
        int total = 0;
        for (RegistroAtencion r : contexto.atenciones.atencionesDelDia(m.getId(), fecha)) {
            Paciente p = contexto.pacientes.buscarPorId(r.pacienteId());
            Especialidad e = contexto.especialidades.buscarPorId(r.atencion().getEspecialidadId());
            modelo.addRow(new Object[]{FechaUtil.formatear(r.atencion().getFechaHora()),
                    p != null ? p.getNombreCompleto() : "—", e != null ? e.getNombre() : "—",
                    r.atencion().getMotivo(), r.atencion().getDiagnostico()});
            total++;
        }
        lblTotal.setText("  Total: " + total);
    }
}
