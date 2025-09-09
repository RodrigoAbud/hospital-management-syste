package com.fiap.atividade3.graphql.resolver;

import com.fiap.atividade3.agendamento.service.AgendamentoService;
import com.fiap.atividade3.historico.service.HistoricoService;
import com.fiap.atividade3.graphql.input.ConsultaInput;
import com.fiap.atividade3.graphql.input.ConsultaUpdateInput;
import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GraphQL resolver for consultation operations
 */
@Controller
public class ConsultaResolver {

    @Autowired
    private AgendamentoService agendamentoService;
    
    @Autowired
    private HistoricoService historicoService;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    /**
     * Get consultation by ID
     */
    @QueryMapping
    public Consulta consulta(@Argument Long id) {
        Usuario usuario = getCurrentUser();
        return agendamentoService.buscarConsultaPorId(id, usuario);
    }

    /**
     * Get current user's consultations (for patients)
     */
    @QueryMapping
    public List<Consulta> minhasConsultas() {
        Usuario usuario = getCurrentUser();
        return historicoService.buscarConsultasPorPaciente(usuario.getId(), usuario);
    }

    /**
     * Get consultations by patient ID
     */
    @QueryMapping
    public List<Consulta> consultasPorPaciente(@Argument Long pacienteId) {
        Usuario usuario = getCurrentUser();
        return historicoService.buscarConsultasPorPaciente(pacienteId, usuario);
    }

    /**
     * Get consultations by doctor ID
     */
    @QueryMapping
    public List<Consulta> consultasPorMedico(@Argument Long medicoId) {
        return historicoService.buscarConsultasPorMedico(medicoId);
    }

    /**
     * Get all consultations (medical staff only)
     */
    @QueryMapping
    public List<Consulta> todasConsultas() {
        return historicoService.buscarTodasConsultas();
    }

    /**
     * Get recent consultations (last 30 days)
     */
    @QueryMapping
    public List<Consulta> consultasRecentes() {
        return historicoService.buscarConsultasRecentes();
    }

    /**
     * Get consultations by date range
     */
    @QueryMapping
    public List<Consulta> consultasPorPeriodo(@Argument LocalDateTime inicio, @Argument LocalDateTime fim) {
        return historicoService.buscarConsultasPorPeriodo(inicio, fim);
    }

    /**
     * Create new consultation
     */
    @MutationMapping
    public Consulta criarConsulta(@Argument ConsultaInput input) {
        Consulta consulta = new Consulta();
        consulta.setDataConsulta(input.getDataConsulta());
        consulta.setMotivo(input.getMotivo());
        consulta.setDiagnostico(input.getDiagnostico());
        consulta.setPrescricao(input.getPrescricao());
        consulta.setObservacoes(input.getObservacoes());

        // Set relationships
        consulta.setPaciente(pacienteRepository.findById(input.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado")));
        consulta.setMedico(medicoRepository.findById(input.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado")));
        
        if (input.getEnfermeiroId() != null) {
            consulta.setEnfermeiro(enfermeiroRepository.findById(input.getEnfermeiroId())
                    .orElseThrow(() -> new RuntimeException("Enfermeiro não encontrado")));
        }

        return agendamentoService.criarConsulta(consulta);
    }

    /**
     * Update consultation
     */
    @MutationMapping
    public Consulta atualizarConsulta(@Argument Long id, @Argument ConsultaUpdateInput input) {
        Consulta consultaAtualizada = new Consulta();
        consultaAtualizada.setDiagnostico(input.getDiagnostico());
        consultaAtualizada.setPrescricao(input.getPrescricao());
        consultaAtualizada.setObservacoes(input.getObservacoes());

        return agendamentoService.atualizarConsulta(id, consultaAtualizada);
    }

    /**
     * Delete consultation
     */
    @MutationMapping
    public Boolean deletarConsulta(@Argument Long id) {
        try {
            agendamentoService.deletarConsulta(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return userDetailsService.findByEmail(authentication.getName());
        }
        throw new RuntimeException("Usuário não autenticado");
    }
}
