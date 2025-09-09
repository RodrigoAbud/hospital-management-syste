package com.fiap.atividade3.historico.service;

import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.model.enums.UserRole;
import com.fiap.atividade3.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pelo histórico e consultas de dados
 * Faz parte da arquitetura de microserviços lógicos
 */
@Service
public class HistoricoService {

    @Autowired
    private ConsultaRepository consultaRepository;

    /**
     * Buscar consultas por paciente com controle de acesso
     */
    public List<Consulta> buscarConsultasPorPaciente(Long pacienteId, Usuario usuario) {
        // Pacientes só podem ver suas próprias consultas
        if (usuario.getRole() == UserRole.PACIENTE && !usuario.getId().equals(pacienteId)) {
            throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas");
        }
        
        return consultaRepository.findByPacienteIdOrderByDataConsultaDesc(pacienteId);
    }

    /**
     * Buscar consultas por médico (Médicos e Enfermeiros)
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasPorMedico(Long medicoId) {
        return consultaRepository.findByMedicoIdOrderByDataConsultaDesc(medicoId);
    }

    /**
     * Buscar todas as consultas (Médicos e Enfermeiros)
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarTodasConsultas() {
        return consultaRepository.findAll();
    }

    /**
     * Buscar consultas por período (Médicos e Enfermeiros)
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByDataConsultaBetween(inicio, fim);
    }

    /**
     * Buscar consultas recentes (últimos 30 dias)
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasRecentes() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return consultaRepository.findRecentConsultations(thirtyDaysAgo);
    }

    /**
     * Buscar consultas futuras de um paciente
     */
    public List<Consulta> buscarConsultasFuturas(Long pacienteId, Usuario usuario) {
        // Pacientes só podem ver suas próprias consultas
        if (usuario.getRole() == UserRole.PACIENTE && !usuario.getId().equals(pacienteId)) {
            throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas");
        }
        
        LocalDateTime agora = LocalDateTime.now();
        return consultaRepository.findByPacienteIdAndDataConsultaAfterOrderByDataConsultaAsc(pacienteId, agora);
    }

    /**
     * Buscar histórico completo de um paciente (apenas para médicos e enfermeiros)
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarHistoricoCompleto(Long pacienteId) {
        return consultaRepository.findByPacienteIdOrderByDataConsultaDesc(pacienteId);
    }

    /**
     * Gerar relatório de consultas por especialidade
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasPorEspecialidade(String especialidade) {
        return consultaRepository.findByMedicoEspecialidadeOrderByDataConsultaDesc(especialidade);
    }

    /**
     * Buscar estatísticas de consultas (para dashboards)
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public ConsultaStats gerarEstatisticas() {
        long totalConsultas = consultaRepository.count();
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long consultasDoMes = consultaRepository.countByDataConsultaAfter(inicioMes);
        
        return new ConsultaStats(totalConsultas, consultasDoMes);
    }

    /**
     * Classe para estatísticas de consultas
     */
    public static class ConsultaStats {
        private final long totalConsultas;
        private final long consultasDoMes;

        public ConsultaStats(long totalConsultas, long consultasDoMes) {
            this.totalConsultas = totalConsultas;
            this.consultasDoMes = consultasDoMes;
        }

        public long getTotalConsultas() {
            return totalConsultas;
        }

        public long getConsultasDoMes() {
            return consultasDoMes;
        }
    }
}
