package com.fiap.atividade3.graphql.type;

import com.fiap.atividade3.model.entity.Usuario;

/**
 * GraphQL type for authentication response
 */
public class AuthPayload {
    
    private String token;
    private Usuario usuario;

    // Constructors
    public AuthPayload() {}

    public AuthPayload(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
