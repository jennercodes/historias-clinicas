package com.posta.util;

import java.nio.file.Path;
import java.nio.file.Paths;

// Resuelve las rutas de los archivos de datos. El directorio base es "data"
// por defecto, pero puede cambiarse (por ejemplo, en las pruebas para usar
// un directorio temporal) mediante setDirectorioBase.
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

    // Ruta de un archivo dentro del directorio de datos.
    public static Path rutaDatos(String nombreArchivo) {
        return directorioBase.resolve(nombreArchivo);
    }
}
