package com.fiap.atividade3.historico.graphql;

import com.fiap.atividade3.historico.service.HistoricoService;
import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GraphQL Resolver para operações de histórico e consultas
 * Faz parte do serviço de histórico na arquitetura de microserviços lógicos
 */
@Controller
public class HistoricoResolver {

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Buscar todas as consultas (com controle de acesso)
     */
    @QueryMapping
    public List<Consulta> consultas() {
        return historicoService.buscarTodasConsultas();
    }

    /**
     * Buscar consultas por paciente
     */
    @QueryMapping
    public List<Consulta> consultasPorPaciente(@Argument Long pacienteId) {
        Usuario usuario = getUsuarioAutenticado();
        return historicoService.buscarConsultasPorPaciente(pacienteId, usuario);
    }

    /**
     * Buscar consultas por médico
     */
    @QueryMapping
    public List<Consulta> consultasPorMedico(@Argument Long medicoId) {
        return historicoService.buscarConsultasPorMedico(medicoId);
    }

    /**
     * Buscar consultas por período
     */
    @QueryMapping
    public List<Consulta> consultasPorPeriodo(@Argument String inicio, @Argument String fim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataInicio = LocalDateTime.parse(inicio, formatter);
        LocalDateTime dataFim = LocalDateTime.parse(fim, formatter);
        
        return historicoService.buscarConsultasPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Buscar consultas recentes (últimos 30 dias)
     */
    @QueryMapping
    public List<Consulta> consultasRecentes() {
        return historicoService.buscarConsultasRecentes();
    }

    /**
     * Buscar consultas futuras de um paciente
     */
    @QueryMapping
    public List<Consulta> consultasFuturas(@Argument Long pacienteId) {
        Usuario usuario = getUsuarioAutenticado();
        return historicoService.buscarConsultasFuturas(pacienteId, usuario);
    }

    /**
     * Buscar histórico completo de um paciente
     */
    @QueryMapping
    public List<Consulta> historicoCompleto(@Argument Long pacienteId) {
        return historicoService.buscarHistoricoCompleto(pacienteId);
    }

    /**
     * Buscar consultas por especialidade
     */
    @QueryMapping
    public List<Consulta> consultasPorEspecialidade(@Argument String especialidade) {
        return historicoService.buscarConsultasPorEspecialidade(especialidade);
    }

    /**
     * Gerar estatísticas de consultas
     */
    @QueryMapping
    public HistoricoService.ConsultaStats estatisticasConsultas() {
        return historicoService.gerarEstatisticas();
    }

    /**
     * Obter usuário autenticado do contexto de segurança
     */
    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return userDetailsService.findByEmail(authentication.getName());
        }
        throw new RuntimeException("Usuário não autenticado");
    }
}
