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


@Service
public class HistoricoService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<Consulta> buscarConsultasPorPaciente(Long pacienteId, Usuario usuario) {
        // Pacientes só podem ver suas próprias consultas
        if (usuario.getRole() == UserRole.PACIENTE && !usuario.getId().equals(pacienteId)) {
            throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas");
        }
        
        return consultaRepository.findByPacienteIdOrderByDataConsultaDesc(pacienteId);
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasPorMedico(Long medicoId) {
        return consultaRepository.findByMedicoIdOrderByDataConsultaDesc(medicoId);
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarTodasConsultas() {
        return consultaRepository.findAll();
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByDataConsultaBetween(inicio, fim);
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasRecentes() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return consultaRepository.findRecentConsultations(thirtyDaysAgo);
    }

    public List<Consulta> buscarConsultasFuturas(Long pacienteId, Usuario usuario) {
        // Pacientes só podem ver suas próprias consultas
        if (usuario.getRole() == UserRole.PACIENTE && !usuario.getId().equals(pacienteId)) {
            throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas");
        }
        
        LocalDateTime agora = LocalDateTime.now();
        return consultaRepository.findByPacienteIdAndDataConsultaAfterOrderByDataConsultaAsc(pacienteId, agora);
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarHistoricoCompleto(Long pacienteId) {
        return consultaRepository.findByPacienteIdOrderByDataConsultaDesc(pacienteId);
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> buscarConsultasPorEspecialidade(String especialidade) {
        return consultaRepository.findByMedicoEspecialidadeOrderByDataConsultaDesc(especialidade);
    }

    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public ConsultaStats gerarEstatisticas() {
        long totalConsultas = consultaRepository.count();
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long consultasDoMes = consultaRepository.countByDataConsultaAfter(inicioMes);
        
        return new ConsultaStats(totalConsultas, consultasDoMes);
    }

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
