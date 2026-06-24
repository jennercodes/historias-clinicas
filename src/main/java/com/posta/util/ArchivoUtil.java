package com.posta.util;

import java.nio.file.Path;
import java.nio.file.Paths;

// Rutas de los archivos de datos. El directorio base es configurable (los tests
// lo apuntan a un directorio temporal).
public final class ArchivoUtil {

    private static Path directorioBase =
            Paths.get(System.getProperty("posta.datadir", "data"));

    private ArchivoUtil() {
    }

    public static Path getDirectorioBase() {
        return directorioBase;
    }

    public static void setDirectorioBase(Path dir) {
        directorioBase = dir;
    }

    public static Path rutaDatos(String nombreArchivo) {
        return directorioBase.resolve(nombreArchivo);
    }
}
