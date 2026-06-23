package com.posta.repository;

import com.posta.model.Rol;
import com.posta.model.Usuario;

// Repositorio de usuarios del sistema. Siembra un administrador por defecto
// la primera vez (cuando aun no existe el archivo de datos).
public class RepositorioUsuario extends RepositorioArreglo<Usuario> {

    public RepositorioUsuario() {
        super("usuarios.dat");
        if (datos.estaVacio()) {
            sembrarAdministrador();
        }
    }

    @Override
    protected int idDe(Usuario u) {
        return u.getId();
    }

    @Override
    protected void asignarId(Usuario u, int id) {
        u.setId(id);
    }

    public Usuario buscarPorUsuario(String usuario) {
        if (usuario == null) {
            return null;
        }
        return datos.buscar(u -> usuario.equalsIgnoreCase(u.getUsuario()));
    }

    private void sembrarAdministrador() {
        guardar(new Usuario(0, "admin", "admin123", "Administrador del Sistema", Rol.ADMINISTRADOR));
    }
}
