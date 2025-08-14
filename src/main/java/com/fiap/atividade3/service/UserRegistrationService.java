package com.fiap.atividade3.service;

import com.fiap.atividade3.graphql.input.MedicoInput;
import com.fiap.atividade3.graphql.input.EnfermeiroInput;
import com.fiap.atividade3.graphql.input.PacienteInput;
import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Enfermeiro;
import com.fiap.atividade3.model.entity.Paciente;
import com.fiap.atividade3.model.enums.UserRole;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import com.fiap.atividade3.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for user registration operations
 */
@Service
@Transactional
public class UserRegistrationService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new medico
     */
    public Medico registrarMedico(MedicoInput input) {
        // Check if email already exists
        if (usuarioRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Check if CRM already exists
        if (medicoRepository.existsByCrm(input.getCrm())) {
            throw new RuntimeException("CRM já está em uso");
        }

        Medico medico = new Medico();
        medico.setNome(input.getNome());
        medico.setEmail(input.getEmail());
        medico.setSenha(passwordEncoder.encode(input.getSenha()));
        medico.setRole(UserRole.MEDICO);
        medico.setCrm(input.getCrm());
        medico.setEspecialidade(input.getEspecialidade());
        medico.setCreatedAt(LocalDateTime.now());
        medico.setActive(true);

        return medicoRepository.save(medico);
    }

    /**
     * Register a new enfermeiro
     */
    public Enfermeiro registrarEnfermeiro(EnfermeiroInput input) {
        // Check if email already exists
        if (usuarioRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Check if COREN already exists
        if (enfermeiroRepository.existsByCoren(input.getCoren())) {
            throw new RuntimeException("COREN já está em uso");
        }

        Enfermeiro enfermeiro = new Enfermeiro();
        enfermeiro.setNome(input.getNome());
        enfermeiro.setEmail(input.getEmail());
        enfermeiro.setSenha(passwordEncoder.encode(input.getSenha()));
        enfermeiro.setRole(UserRole.ENFERMEIRO);
        enfermeiro.setCoren(input.getCoren());
        enfermeiro.setSetor(input.getSetor());
        enfermeiro.setCreatedAt(LocalDateTime.now());
        enfermeiro.setActive(true);

        return enfermeiroRepository.save(enfermeiro);
    }

    /**
     * Register a new paciente
     */
    public Paciente registrarPaciente(PacienteInput input) {
        // Check if email already exists
        if (usuarioRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Check if CPF already exists
        if (pacienteRepository.existsByCpf(input.getCpf())) {
            throw new RuntimeException("CPF já está em uso");
        }

        Paciente paciente = new Paciente();
        paciente.setNome(input.getNome());
        paciente.setEmail(input.getEmail());
        paciente.setSenha(passwordEncoder.encode(input.getSenha()));
        paciente.setRole(UserRole.PACIENTE);
        paciente.setCpf(input.getCpf());
        paciente.setDataNascimento(input.getDataNascimento());
        paciente.setTelefone(input.getTelefone());
        paciente.setEndereco(input.getEndereco());
        paciente.setCreatedAt(LocalDateTime.now());
        paciente.setActive(true);

        return pacienteRepository.save(paciente);
    }
}
