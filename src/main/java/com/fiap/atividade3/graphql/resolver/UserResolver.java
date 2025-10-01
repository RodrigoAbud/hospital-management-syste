package com.fiap.atividade3.graphql.resolver;

import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Enfermeiro;
import com.fiap.atividade3.model.entity.Paciente;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserResolver {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

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


}
