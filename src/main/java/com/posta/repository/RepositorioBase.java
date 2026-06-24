package com.posta.repository;

import com.posta.util.ArchivoUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

// Lee y escribe la estructura de datos completa en un archivo mediante
// serializacion. La escritura es atomica (archivo temporal + rename) para no
// corromper los datos ante un fallo a mitad de guardado (RNF03).
public abstract class RepositorioBase<C extends Serializable> {

    private final String nombreArchivo;
    private final Supplier<C> fabricaVacia;
    protected C datos;

    protected RepositorioBase(String nombreArchivo, Supplier<C> fabricaVacia) {
        this.nombreArchivo = nombreArchivo;
        this.fabricaVacia = fabricaVacia;
        this.datos = cargar();
    }

    private Path archivo() {
        return ArchivoUtil.rutaDatos(nombreArchivo);
    }

    @SuppressWarnings("unchecked")
    private C cargar() {
        Path archivo = archivo();
        if (!Files.exists(archivo)) {
            return fabricaVacia.get();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(archivo))) {
            return (C) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositorioException("No se pudo cargar el archivo de datos: " + archivo, e);
        }
    }

    protected final void persistir() {
        Path archivo = archivo();
        try {
            Path directorio = archivo.toAbsolutePath().getParent();
            if (directorio != null) {
                Files.createDirectories(directorio);
            }
            Path temporal = archivo.resolveSibling(nombreArchivo + ".tmp");
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(temporal))) {
                out.writeObject(datos);
            }
            mover(temporal, archivo);
        } catch (IOException e) {
            throw new RepositorioException("No se pudo guardar el archivo de datos: " + archivo, e);
        }
    }

    private void mover(Path origen, Path destino) throws IOException {
        try {
            Files.move(origen, destino,
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException noAtomico) {
            // Algunos sistemas de archivos no soportan el movimiento atomico
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
