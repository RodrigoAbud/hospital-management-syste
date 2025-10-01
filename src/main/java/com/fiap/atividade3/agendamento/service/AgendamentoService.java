package com.fiap.atividade3.agendamento.service;

import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.model.enums.UserRole;
import com.fiap.atividade3.repository.ConsultaRepository;
import com.fiap.atividade3.notificacao.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviço responsável pelo agendamento e gestão de consultas
 * Faz parte da arquitetura de microserviços lógicos
 */
@Service
@Transactional
public class AgendamentoService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Criar nova consulta (Enfermeiros e Médicos)
     * Envia evento assíncrono para notificações
     */
    @PreAuthorize("hasRole('ENFERMEIRO') or hasRole('MEDICO')")
    public Consulta criarConsulta(Consulta consulta) {
        Consulta savedConsulta = consultaRepository.save(consulta);
        
        // Enviar evento assíncrono para o serviço de notificações
        notificacaoService.processarConsultaCriada(savedConsulta);
        
        return savedConsulta;
    }

    /**
     * Atualizar consulta existente (apenas Médicos)
     * Envia evento assíncrono para notificações
     */
    @PreAuthorize("hasRole('MEDICO')")
    public Consulta atualizarConsulta(Long id, Consulta consultaAtualizada) {
        Optional<Consulta> consultaExistente = consultaRepository.findById(id);
        if (consultaExistente.isPresent()) {
            Consulta consulta = consultaExistente.get();
            consulta.setDiagnostico(consultaAtualizada.getDiagnostico());
            consulta.setPrescricao(consultaAtualizada.getPrescricao());
            consulta.setObservacoes(consultaAtualizada.getObservacoes());
            
            Consulta savedConsulta = consultaRepository.save(consulta);
            
            // Enviar evento assíncrono para o serviço de notificações
            notificacaoService.processarConsultaAtualizada(savedConsulta);
            
            return savedConsulta;
        }
        throw new RuntimeException("Consulta não encontrada");
    }

    /**
     * Deletar consulta (apenas Médicos)
     */
    @PreAuthorize("hasRole('MEDICO')")
    public void deletarConsulta(Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    /**
     * Buscar consulta por ID com controle de acesso
     */
    public Consulta buscarConsultaPorId(Long id, Usuario usuario) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        if (consulta.isPresent()) {
            Consulta c = consulta.get();
            
            // Pacientes só podem ver suas próprias consultas
            if (usuario.getRole() == UserRole.PACIENTE) {
                if (!c.getPaciente().getId().equals(usuario.getId())) {
                    throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas");
                }
            }
            
            return c;
        }
        throw new RuntimeException("Consulta não encontrada");
    }
}
