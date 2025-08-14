package com.fiap.atividade3.graphql.resolver;

import com.fiap.atividade3.graphql.input.LoginInput;
import com.fiap.atividade3.graphql.input.MedicoInput;
import com.fiap.atividade3.graphql.input.EnfermeiroInput;
import com.fiap.atividade3.graphql.input.PacienteInput;
import com.fiap.atividade3.graphql.type.AuthPayload;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Enfermeiro;
import com.fiap.atividade3.model.entity.Paciente;
import com.fiap.atividade3.service.AuthService;
import com.fiap.atividade3.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

/**
 * GraphQL resolver for authentication operations
 */
@Controller
public class AuthResolver {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    /**
     * Login mutation
     */
    @MutationMapping
    public AuthPayload login(@Argument LoginInput input) {
        String token = authService.login(input.getEmail(), input.getSenha());
        Usuario usuario = authService.getUserFromToken(token);
        return new AuthPayload(token, usuario);
    }

    /**
     * Register a new medico
     */
    @MutationMapping
    public Medico registrarMedico(@Argument MedicoInput input) {
        return userRegistrationService.registrarMedico(input);
    }

    /**
     * Register a new enfermeiro
     */
    @MutationMapping
    public Enfermeiro registrarEnfermeiro(@Argument EnfermeiroInput input) {
        return userRegistrationService.registrarEnfermeiro(input);
    }

    /**
     * Register a new paciente
     */
    @MutationMapping
    public Paciente registrarPaciente(@Argument PacienteInput input) {
        return userRegistrationService.registrarPaciente(input);
    }

    /**
     * Get current authenticated user
     */
    @QueryMapping
    public Usuario me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuário não autenticado");
    }
}
