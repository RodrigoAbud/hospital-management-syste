package com.fiap.atividade3.notificacao.listener;

import com.fiap.atividade3.config.AsyncConfig;
import com.fiap.atividade3.model.entity.Consulta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ConsultaEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaEventListener.class);


    @RabbitListener(queues = AsyncConfig.CONSULTA_CREATED_QUEUE)
    public void handleConsultaCreated(Consulta consulta) {
        logger.info("🎯 [NOTIFICAÇÃO] Recebido evento de consulta criada - ID: {}", consulta.getId());
        
        try {
            // Processar notificações automáticas
            processarNotificacaoConsultaCriada(consulta);
            
            // Log para auditoria
            logger.info("✅ [NOTIFICAÇÃO] Consulta criada processada com sucesso - Paciente: {}, Médico: {}, Data: {}", 
                       consulta.getPaciente().getNome(),
                       consulta.getMedico().getNome(),
                       consulta.getDataConsulta());
                       
        } catch (Exception e) {
            logger.error("❌ [NOTIFICAÇÃO] Erro ao processar consulta criada: {}", e.getMessage());
        }
    }


    @RabbitListener(queues = AsyncConfig.CONSULTA_UPDATED_QUEUE)
    public void handleConsultaUpdated(Consulta consulta) {
        logger.info("🎯 [NOTIFICAÇÃO] Recebido evento de consulta atualizada - ID: {}", consulta.getId());
        
        try {
            // Processar notificações de atualização
            processarNotificacaoConsultaAtualizada(consulta);
            
            // Log para auditoria
            logger.info("✅ [NOTIFICAÇÃO] Consulta atualizada processada com sucesso - ID: {}, Atualizada por: {}", 
                       consulta.getId(),
                       consulta.getMedico().getNome());
                       
        } catch (Exception e) {
            logger.error("❌ [NOTIFICAÇÃO] Erro ao processar consulta atualizada: {}", e.getMessage());
        }
    }


    private void processarNotificacaoConsultaCriada(Consulta consulta) {
        // Simular processamento de notificações
        logger.info("📧 Enviando confirmação de agendamento para: {}", consulta.getPaciente().getEmail());
        logger.info("📱 Enviando SMS de lembrete para: {}", consulta.getPaciente().getTelefone());
        
        // Aqui você implementaria:
        // - Envio de email de confirmação
        // - Agendamento de lembretes automáticos
        // - Notificação para equipe médica
        // - Atualização de estatísticas
        
        // Simular delay de processamento
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void processarNotificacaoConsultaAtualizada(Consulta consulta) {
        // Simular processamento de notificações de atualização
        logger.info("📧 Enviando atualização de consulta para: {}", consulta.getPaciente().getEmail());
        
        if (consulta.getDiagnostico() != null && !consulta.getDiagnostico().isEmpty()) {
            logger.info("🏥 Diagnóstico disponível - enviando notificação detalhada");
        }
        
        if (consulta.getPrescricao() != null && !consulta.getPrescricao().isEmpty()) {
            logger.info("💊 Prescrição disponível - enviando para farmácia parceira");
        }
        
        // Aqui você implementaria:
        // - Notificação sobre mudanças no diagnóstico
        // - Envio de prescrições para farmácias
        // - Agendamento de consultas de retorno
        // - Atualização do histórico médico
    }
}
