package com.fiap.atividade3.controller;

import com.fiap.atividade3.graphql.input.LoginInput;
import com.fiap.atividade3.graphql.type.AuthPayload;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication operations
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação e autorização")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    @Operation(summary = "Login do usuário", description = "Autentica o usuário e retorna um token JWT")
    public ResponseEntity<AuthPayload> login(@RequestBody LoginInput loginInput) {
        try {
            String token = authService.login(loginInput.getEmail(), loginInput.getSenha());
            Usuario usuario = authService.getUserFromToken(token);
            AuthPayload authPayload = new AuthPayload(token, usuario);
            return ResponseEntity.ok(authPayload);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get current user info
     */
    @GetMapping("/me")
    @Operation(summary = "Informações do usuário atual", description = "Retorna informações do usuário autenticado")
    public ResponseEntity<Usuario> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return ResponseEntity.ok((Usuario) authentication.getPrincipal());
        }
        return ResponseEntity.status(401).build();
    }

    /**
     * Validate token
     */
    @PostMapping("/validate")
    @Operation(summary = "Validar token", description = "Valida se um token JWT é válido")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
