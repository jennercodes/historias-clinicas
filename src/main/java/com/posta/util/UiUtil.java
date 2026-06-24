package com.posta.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Atajos para los dialogos mas usados de la interfaz.
public final class UiUtil {

    private UiUtil() {
    }

    // Abre un archivo con la aplicacion por defecto del sistema.
    public static void abrir(Component padre, Path archivo) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo.toFile());
            }
        } catch (IOException e) {
            info(padre, "Archivo generado, pero no se pudo abrir automaticamente:\n" + archivo);
        }
    }

    // Nombres de columna de un modelo de tabla.
    public static String[] columnas(DefaultTableModel modelo) {
        String[] columnas = new String[modelo.getColumnCount()];
        for (int i = 0; i < columnas.length; i++) {
            columnas[i] = modelo.getColumnName(i);
        }
        return columnas;
    }

    // Filas de un modelo de tabla como texto.
    public static List<String[]> filas(DefaultTableModel modelo) {
        List<String[]> filas = new ArrayList<>();
        for (int f = 0; f < modelo.getRowCount(); f++) {
            String[] fila = new String[modelo.getColumnCount()];
            for (int c = 0; c < fila.length; c++) {
                Object valor = modelo.getValueAt(f, c);
                fila[c] = valor == null ? "" : String.valueOf(valor);
            }
            filas.add(fila);
        }
        return filas;
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component padre, String mensaje) {
        int r = JOptionPane.showConfirmDialog(padre, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }

    public static JLabel titulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 18f));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return etiqueta;
    }
}
