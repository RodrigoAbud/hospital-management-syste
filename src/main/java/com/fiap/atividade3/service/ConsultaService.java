package com.fiap.atividade3.service;

import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.model.enums.UserRole;
import com.fiap.atividade3.repository.ConsultaRepository;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private AsyncMessagingService asyncMessagingService;


    @PreAuthorize("hasRole('ENFERMEIRO') or hasRole('MEDICO')")
    public Consulta criarConsulta(Consulta consulta) {
        Consulta savedConsulta = consultaRepository.save(consulta);
        // Publish async event for consultation creation
        asyncMessagingService.publishConsultaCreated(savedConsulta);
        return savedConsulta;
    }


    @PreAuthorize("hasRole('MEDICO')")
    public Consulta atualizarConsulta(Long id, Consulta consultaAtualizada) {
        Optional<Consulta> consultaExistente = consultaRepository.findById(id);
        if (consultaExistente.isPresent()) {
            Consulta consulta = consultaExistente.get();
            consulta.setDiagnostico(consultaAtualizada.getDiagnostico());
            consulta.setPrescricao(consultaAtualizada.getPrescricao());
            consulta.setObservacoes(consultaAtualizada.getObservacoes());
            Consulta savedConsulta = consultaRepository.save(consulta);
            // Publish async event for consultation update
            asyncMessagingService.publishConsultaUpdated(savedConsulta);
            return savedConsulta;
        }
        throw new RuntimeException("Consulta não encontrada");
    }


    public Consulta buscarConsultaPorId(Long id, Usuario usuario) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        if (consulta.isPresent()) {
            Consulta c = consulta.get();
            
            // Pacientes can only view their own consultations
            if (usuario.getRole() == UserRole.PACIENTE) {
                if (!c.getPaciente().getId().equals(usuario.getId())) {
                    throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas");
                }
            }
            
            return c;
        }
        throw new RuntimeException("Consulta não encontrada");
    }


    public List<Consulta> buscarConsultasPorPaciente(Long pacienteId, Usuario usuario) {
        // Pacientes can only view their own consultations
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

    @PreAuthorize("hasRole('MEDICO')")
    public void deletarConsulta(Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }
}
