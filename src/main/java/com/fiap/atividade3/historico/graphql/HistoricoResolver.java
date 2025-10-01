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


@Controller
public class HistoricoResolver {

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @QueryMapping
    public List<Consulta> consultas() {
        return historicoService.buscarTodasConsultas();
    }

    @QueryMapping
    public List<Consulta> consultasPorPaciente(@Argument Long pacienteId) {
        Usuario usuario = getUsuarioAutenticado();
        return historicoService.buscarConsultasPorPaciente(pacienteId, usuario);
    }

    @QueryMapping
    public List<Consulta> consultasPorMedico(@Argument Long medicoId) {
        return historicoService.buscarConsultasPorMedico(medicoId);
    }

    @QueryMapping
    public List<Consulta> consultasPorPeriodo(@Argument String inicio, @Argument String fim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataInicio = LocalDateTime.parse(inicio, formatter);
        LocalDateTime dataFim = LocalDateTime.parse(fim, formatter);
        
        return historicoService.buscarConsultasPorPeriodo(dataInicio, dataFim);
    }

    @QueryMapping
    public List<Consulta> consultasRecentes() {
        return historicoService.buscarConsultasRecentes();
    }

    @QueryMapping
    public List<Consulta> consultasFuturas(@Argument Long pacienteId) {
        Usuario usuario = getUsuarioAutenticado();
        return historicoService.buscarConsultasFuturas(pacienteId, usuario);
    }

    @QueryMapping
    public List<Consulta> historicoCompleto(@Argument Long pacienteId) {
        return historicoService.buscarHistoricoCompleto(pacienteId);
    }

    @QueryMapping
    public List<Consulta> consultasPorEspecialidade(@Argument String especialidade) {
        return historicoService.buscarConsultasPorEspecialidade(especialidade);
    }

    @QueryMapping
    public HistoricoService.ConsultaStats estatisticasConsultas() {
        return historicoService.gerarEstatisticas();
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return userDetailsService.findByEmail(authentication.getName());
        }
        throw new RuntimeException("Usuário não autenticado");
    }
}
