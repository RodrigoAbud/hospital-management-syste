package com.fiap.atividade3.agendamento.graphql;

import com.fiap.atividade3.agendamento.service.AgendamentoService;
import com.fiap.atividade3.graphql.input.ConsultaInput;
import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Enfermeiro;
import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Paciente;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


@Controller
public class AgendamentoResolver {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    @MutationMapping
    public Consulta criarConsulta(@Argument ConsultaInput input) {
        Consulta consulta = new Consulta();
        
        // Buscar entidades relacionadas
        Medico medico = medicoRepository.findById(input.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        Paciente paciente = pacienteRepository.findById(input.getPacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        // Configurar consulta
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setMotivo(input.getMotivo());
        consulta.setObservacoes(input.getObservacoes());
        
        // Parse da data (assumindo que input.getDataConsulta() já é LocalDateTime)
        consulta.setDataConsulta(input.getDataConsulta());
        
        // Enfermeiro opcional
        if (input.getEnfermeiroId() != null) {
            Enfermeiro enfermeiro = enfermeiroRepository.findById(input.getEnfermeiroId())
                .orElseThrow(() -> new RuntimeException("Enfermeiro não encontrado"));
            consulta.setEnfermeiro(enfermeiro);
        }
        
        return agendamentoService.criarConsulta(consulta);
    }

    @MutationMapping
    public Consulta atualizarConsulta(@Argument Long id, @Argument ConsultaInput input) {
        Consulta consultaAtualizada = new Consulta();
        consultaAtualizada.setDiagnostico(input.getDiagnostico());
        consultaAtualizada.setPrescricao(input.getPrescricao());
        consultaAtualizada.setObservacoes(input.getObservacoes());
        
        return agendamentoService.atualizarConsulta(id, consultaAtualizada);
    }

    @MutationMapping
    public Boolean deletarConsulta(@Argument Long id) {
        try {
            agendamentoService.deletarConsulta(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
