package com.posta.service;

import com.posta.model.Usuario;
import com.posta.repository.RepositorioUsuario;

import java.util.Optional;

// Autenticacion de usuarios (RF01).
public class AuthService {

    private final RepositorioUsuario repositorio;

    public AuthService(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<Usuario> login(String usuario, String clave) {
        Usuario encontrado = repositorio.buscarPorUsuario(usuario);
        if (encontrado != null && encontrado.getClave().equals(clave)) {
            return Optional.of(encontrado);
        }
        return Optional.empty();
    }
}
