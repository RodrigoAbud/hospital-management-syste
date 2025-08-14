package com.fiap.atividade3.graphql.resolver;

import com.fiap.atividade3.graphql.input.MedicoInput;
import com.fiap.atividade3.graphql.input.EnfermeiroInput;
import com.fiap.atividade3.graphql.input.PacienteInput;
import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Enfermeiro;
import com.fiap.atividade3.model.entity.Paciente;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import com.fiap.atividade3.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL resolver for user management operations
 */
@Controller
public class UserResolver {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AuthService authService;

    // Queries - Medical staff only
    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Medico> medicos() {
        return medicoRepository.findAllActiveTrue();
    }

    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Enfermeiro> enfermeiros() {
        return enfermeiroRepository.findAllActiveTrue();
    }

    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Paciente> pacientes() {
        return pacienteRepository.findAllActiveTrue();
    }

    // Search queries
    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Medico> buscarMedicos(@Argument String nome, @Argument String especialidade) {
        if (nome != null && !nome.trim().isEmpty()) {
            return medicoRepository.findByNomeContainingIgnoreCaseAndActiveTrue(nome);
        } else if (especialidade != null && !especialidade.trim().isEmpty()) {
            return medicoRepository.findByEspecialidadeAndActiveTrue(especialidade);
        }
        return medicoRepository.findAllActiveTrue();
    }

    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Enfermeiro> buscarEnfermeiros(@Argument String nome, @Argument String setor) {
        if (nome != null && !nome.trim().isEmpty()) {
            return enfermeiroRepository.findByNomeContainingIgnoreCaseAndActiveTrue(nome);
        } else if (setor != null && !setor.trim().isEmpty()) {
            return enfermeiroRepository.findBySetorAndActiveTrue(setor);
        }
        return enfermeiroRepository.findAllActiveTrue();
    }

    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Paciente> buscarPacientes(@Argument String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            return pacienteRepository.findByNomeContainingIgnoreCaseAndActiveTrue(nome);
        }
        return pacienteRepository.findAllActiveTrue();
    }

    // Registration mutations
    @MutationMapping
    public Medico registrarMedico(@Argument MedicoInput input) {
        // Check if CRM already exists
        if (medicoRepository.existsByCrm(input.getCrm())) {
            throw new RuntimeException("CRM já está em uso");
        }

        Medico medico = new Medico(
            input.getNome(),
            input.getEmail(),
            input.getSenha(),
            input.getCrm(),
            input.getEspecialidade()
        );

        authService.register(medico);
        return medicoRepository.save(medico);
    }

    @MutationMapping
    public Enfermeiro registrarEnfermeiro(@Argument EnfermeiroInput input) {
        // Check if COREN already exists
        if (enfermeiroRepository.existsByCoren(input.getCoren())) {
            throw new RuntimeException("COREN já está em uso");
        }

        Enfermeiro enfermeiro = new Enfermeiro(
            input.getNome(),
            input.getEmail(),
            input.getSenha(),
            input.getCoren(),
            input.getSetor()
        );

        authService.register(enfermeiro);
        return enfermeiroRepository.save(enfermeiro);
    }

    @MutationMapping
    public Paciente registrarPaciente(@Argument PacienteInput input) {
        // Check if CPF already exists
        if (pacienteRepository.existsByCpf(input.getCpf())) {
            throw new RuntimeException("CPF já está em uso");
        }

        Paciente paciente = new Paciente(
            input.getNome(),
            input.getEmail(),
            input.getSenha(),
            input.getCpf(),
            input.getDataNascimento()
        );
        
        paciente.setTelefone(input.getTelefone());
        paciente.setEndereco(input.getEndereco());

        authService.register(paciente);
        return pacienteRepository.save(paciente);
    }
}
