package com.fiap.atividade3.graphql.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Input type for login mutation
 */
public class LoginInput {
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    // Constructors
    public LoginInput() {}

    public LoginInput(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
