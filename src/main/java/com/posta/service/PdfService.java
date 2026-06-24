package com.posta.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.posta.model.Atencion;
import com.posta.model.Especialidad;
import com.posta.model.HistoriaClinica;
import com.posta.model.Medico;
import com.posta.model.Paciente;
import com.posta.repository.RepositorioEspecialidad;
import com.posta.repository.RepositorioMedico;
import com.posta.util.FechaUtil;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

// Genera la historia clinica de un paciente en formato PDF (RF10).
public class PdfService {

    private static final Color COLOR_CABECERA = new Color(0x2C3E50);

    private final RepositorioMedico medicos;
    private final RepositorioEspecialidad especialidades;

    public PdfService(RepositorioMedico medicos, RepositorioEspecialidad especialidades) {
        this.medicos = medicos;
        this.especialidades = especialidades;
    }

    // Escribe el PDF de la historia en la ruta indicada y la devuelve.
    public Path generar(Paciente paciente, HistoriaClinica historia, Path destino) throws IOException {
        Path directorio = destino.toAbsolutePath().getParent();
        if (directorio != null) {
            Files.createDirectories(directorio);
        }
        Document documento = new Document(PageSize.A4, 40, 40, 54, 40);
        try {
            PdfWriter.getInstance(documento, Files.newOutputStream(destino));
            documento.open();
            agregarEncabezado(documento);
            agregarDatosPaciente(documento, paciente, historia);
            agregarTablaAtenciones(documento, historia);
            agregarPie(documento);
            documento.close();
        } catch (DocumentException e) {
            throw new IOException("No se pudo generar el PDF", e);
        }
        return destino;
    }

    private void agregarEncabezado(Document documento) throws DocumentException {
        Paragraph posta = new Paragraph("POSTA MEDICA", new Font(Font.HELVETICA, 16, Font.BOLD));
        posta.setAlignment(Element.ALIGN_CENTER);
        documento.add(posta);

        Paragraph titulo = new Paragraph("HISTORIA CLINICA", new Font(Font.HELVETICA, 13, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(12);
        documento.add(titulo);
    }

    private void agregarDatosPaciente(Document documento, Paciente p, HistoriaClinica h) throws DocumentException {
        Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);

        documento.add(new Paragraph("Paciente: " + p.getNombreCompleto(), new Font(Font.HELVETICA, 11, Font.BOLD)));
        documento.add(new Paragraph("DNI: " + p.getDni()
                + "     Sexo: " + p.getSexo()
                + "     Edad: " + p.getEdad() + " anios", normal));
        documento.add(new Paragraph("Fecha de nacimiento: " + FechaUtil.formatear(p.getFechaNacimiento()), normal));
        documento.add(new Paragraph("Direccion: " + texto(p.getDireccion())
                + "     Telefono: " + texto(p.getTelefono()), normal));

        documento.add(new Paragraph("Historia N: " + h.getId()
                + "     Apertura: " + FechaUtil.formatear(h.getFechaApertura()), normal));
        if (noVacio(h.getGrupoSanguineo()) || noVacio(h.getAlergias())) {
            documento.add(new Paragraph("Grupo sanguineo: " + texto(h.getGrupoSanguineo())
                    + "     Alergias: " + texto(h.getAlergias()), normal));
        }
        if (noVacio(h.getAntecedentes())) {
            documento.add(new Paragraph("Antecedentes: " + h.getAntecedentes(), normal));
        }

        Paragraph subtitulo = new Paragraph("Atenciones", new Font(Font.HELVETICA, 12, Font.BOLD));
        subtitulo.setSpacingBefore(12);
        subtitulo.setSpacingAfter(6);
        documento.add(subtitulo);
    }

    private void agregarTablaAtenciones(Document documento, HistoriaClinica h) throws DocumentException {
        if (h.getAtenciones().estaVacia()) {
            documento.add(new Paragraph("Sin atenciones registradas.", new Font(Font.HELVETICA, 10, Font.ITALIC)));
            return;
        }

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2.0f, 2.4f, 2.0f, 2.4f, 2.4f, 2.4f});

        for (String titulo : new String[]{"Fecha", "Medico", "Especialidad", "Motivo", "Diagnostico", "Tratamiento"}) {
            PdfPCell celda = new PdfPCell(new Phrase(titulo, new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE)));
            celda.setBackgroundColor(COLOR_CABECERA);
            celda.setPadding(4);
            tabla.addCell(celda);
        }

        Font fuenteCelda = new Font(Font.HELVETICA, 9, Font.NORMAL);
        // Orden cronologico: de la atencion mas antigua a la mas reciente.
        for (Atencion a : h.getAtenciones()) {
            tabla.addCell(celda(FechaUtil.formatear(a.getFechaHora()), fuenteCelda));
            tabla.addCell(celda(nombreMedico(a.getMedicoId()), fuenteCelda));
            tabla.addCell(celda(nombreEspecialidad(a.getEspecialidadId()), fuenteCelda));
            tabla.addCell(celda(texto(a.getMotivo()), fuenteCelda));
            tabla.addCell(celda(texto(a.getDiagnostico()), fuenteCelda));
            tabla.addCell(celda(texto(a.getTratamiento()), fuenteCelda));
        }
        documento.add(tabla);
    }

    private void agregarPie(Document documento) throws DocumentException {
        Paragraph pie = new Paragraph("Documento generado el " + FechaUtil.formatear(LocalDateTime.now()),
                new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY));
        pie.setAlignment(Element.ALIGN_RIGHT);
        pie.setSpacingBefore(16);
        documento.add(pie);
    }

    private PdfPCell celda(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(3);
        return celda;
    }

    private String nombreMedico(int id) {
        Medico m = medicos.buscarPorId(id);
        return m != null ? m.getNombreCompleto() : "—";
    }

    private String nombreEspecialidad(int id) {
        Especialidad e = especialidades.buscarPorId(id);
        return e != null ? e.getNombre() : "—";
    }

    private boolean noVacio(String s) {
        return s != null && !s.isBlank();
    }

    private String texto(String s) {
        return noVacio(s) ? s : "—";
    }
}
