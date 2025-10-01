package com.fiap.atividade3.notificacao.service;

import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.service.AsyncMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoService.class);

    @Autowired
    private AsyncMessagingService asyncMessagingService;


    public void processarConsultaCriada(Consulta consulta) {
        logger.info("Processando notificação para consulta criada - ID: {}", consulta.getId());
        
        // Enviar evento assíncrono via RabbitMQ
        asyncMessagingService.publishConsultaCreated(consulta);
        
        // Aqui você pode implementar lógicas adicionais como:
        // - Envio de email para o paciente
        // - Notificação push
        // - SMS de confirmação
        // - Integração com sistemas externos
        
        enviarNotificacaoPaciente(consulta, "CONSULTA_CRIADA");
    }


    public void processarConsultaAtualizada(Consulta consulta) {
        logger.info("Processando notificação para consulta atualizada - ID: {}", consulta.getId());
        
        // Enviar evento assíncrono via RabbitMQ
        asyncMessagingService.publishConsultaUpdated(consulta);
        
        enviarNotificacaoPaciente(consulta, "CONSULTA_ATUALIZADA");
    }


    private void enviarNotificacaoPaciente(Consulta consulta, String tipoEvento) {
        try {
            String mensagem = construirMensagemNotificacao(consulta, tipoEvento);
            
            // Simular envio de notificação
            logger.info("📧 Enviando notificação para paciente: {}", consulta.getPaciente().getEmail());
            logger.info("📱 Mensagem: {}", mensagem);
            
            // Aqui você implementaria a integração real com:
            // - Serviço de email (SendGrid, AWS SES)
            // - Serviço de SMS (Twilio)
            // - Push notifications (Firebase)
            
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação para paciente: {}", e.getMessage());
        }
    }


    private String construirMensagemNotificacao(Consulta consulta, String tipoEvento) {
        switch (tipoEvento) {
            case "CONSULTA_CRIADA":
                return String.format(
                    "Olá %s! Sua consulta foi agendada para %s com Dr(a). %s. Motivo: %s",
                    consulta.getPaciente().getNome(),
                    consulta.getDataConsulta(),
                    consulta.getMedico().getNome(),
                    consulta.getMotivo()
                );
            case "CONSULTA_ATUALIZADA":
                return String.format(
                    "Olá %s! Sua consulta do dia %s foi atualizada. Diagnóstico: %s",
                    consulta.getPaciente().getNome(),
                    consulta.getDataConsulta(),
                    consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "Em análise"
                );
            default:
                return "Você tem uma atualização sobre sua consulta.";
        }
    }


    public void processarLembretesAutomaticos() {
        logger.info("🔔 Processando lembretes automáticos de consultas futuras...");
        
        // Aqui você implementaria:
        // - Buscar consultas nas próximas 24h
        // - Enviar lembretes automáticos
        // - Marcar lembretes como enviados
    }
}
