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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

// Reporte de atenciones por especialidad y rango de fechas (RF09).
public class ReporteEspecialidadPanel extends JPanel {

    private final transient Contexto contexto;

    private final JComboBox<Especialidad> cboEspecialidad = new JComboBox<>();
    private final JTextField txtDesde = new JTextField(10);
    private final JTextField txtHasta = new JTextField(10);
    private final JLabel lblTotal = new JLabel(" ");
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Fecha", "Paciente", "Medico", "Motivo", "Diagnostico"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public ReporteEspecialidadPanel(Contexto contexto) {
        this.contexto = contexto;
        construir();
        cargarEspecialidades();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(UiUtil.titulo("Atenciones por Especialidad"), BorderLayout.NORTH);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.add(new JLabel("Especialidad:"));
        filtros.add(cboEspecialidad);
        filtros.add(new JLabel("Desde:"));
        txtDesde.setText(FechaUtil.formatear(LocalDate.now().withDayOfYear(1)));
        filtros.add(txtDesde);
        filtros.add(new JLabel("Hasta:"));
        txtHasta.setText(FechaUtil.formatear(LocalDate.now()));
        filtros.add(txtHasta);
        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(e -> generar());
        filtros.add(btnGenerar);
        JButton btnExportar = new JButton("Exportar PDF");
        btnExportar.addActionListener(e -> exportarPdf());
        filtros.add(btnExportar);
        filtros.add(lblTotal);
        norte.add(filtros, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void cargarEspecialidades() {
        cboEspecialidad.removeAllItems();
        for (Especialidad e : contexto.especialidades.listar()) {
            cboEspecialidad.addItem(e);
        }
    }

    private void generar() {
        Especialidad esp = (Especialidad) cboEspecialidad.getSelectedItem();
        if (esp == null) {
            UiUtil.error(this, "Seleccione una especialidad.");
            return;
        }
        LocalDate desde = FechaUtil.parsear(txtDesde.getText());
        LocalDate hasta = FechaUtil.parsear(txtHasta.getText());
        if (desde == null || hasta == null) {
            UiUtil.error(this, "Las fechas no son validas (use dd/MM/yyyy).");
            return;
        }
        if (desde.isAfter(hasta)) {
            UiUtil.error(this, "La fecha 'desde' no puede ser posterior a 'hasta'.");
            return;
        }

        modelo.setRowCount(0);
        int total = 0;
        for (RegistroAtencion r : contexto.reportes.porEspecialidadYRango(esp.getId(), desde, hasta)) {
            Paciente p = contexto.pacientes.buscarPorId(r.pacienteId());
            Medico m = contexto.medicos.buscarPorId(r.atencion().getMedicoId());
            modelo.addRow(new Object[]{FechaUtil.formatear(r.atencion().getFechaHora()),
                    p != null ? p.getNombreCompleto() : "—", m != null ? m.getNombreCompleto() : "—",
                    r.atencion().getMotivo(), r.atencion().getDiagnostico()});
            total++;
        }
        lblTotal.setText("  Total: " + total);
    }

    private void exportarPdf() {
        if (modelo.getRowCount() == 0) {
            UiUtil.info(this, "Genere primero el reporte.");
            return;
        }
        Especialidad esp = (Especialidad) cboEspecialidad.getSelectedItem();
        String desde = txtDesde.getText().trim();
        String hasta = txtHasta.getText().trim();
        String subtitulo = "Especialidad: " + (esp != null ? esp.getNombre() : "—")
                + "     Periodo: " + desde + " a " + hasta;
        Path destino = Paths.get("reportes-pdf",
                "reporte-especialidad-" + (esp != null ? esp.getId() : "x")
                        + "-" + desde.replace("/", "-") + "_" + hasta.replace("/", "-") + ".pdf");
        try {
            contexto.pdf.generarTabla("Atenciones por Especialidad", subtitulo,
                    UiUtil.columnas(modelo), UiUtil.filas(modelo), destino);
            UiUtil.abrir(this, destino);
            UiUtil.info(this, "PDF generado en:\n" + destino.toAbsolutePath());
        } catch (IOException ex) {
            UiUtil.error(this, "No se pudo generar el PDF: " + ex.getMessage());
        }
    }
}
