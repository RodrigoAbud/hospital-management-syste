package com.fiap.atividade3.service;

import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.repository.UsuarioRepository;
import com.fiap.atividade3.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String senha) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            return jwtUtil.generateToken(usuario);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas", e);
        }
    }


    public Usuario register(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Encode password
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        return usuarioRepository.save(usuario);
    }


    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }


    public Usuario getUserFromToken(String token) {
        String email = jwtUtil.extractUsername(token);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
