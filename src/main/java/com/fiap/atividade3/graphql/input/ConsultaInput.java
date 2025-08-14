package com.fiap.atividade3.graphql.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Input type for creating consultations
 */
public class ConsultaInput {
    
    @NotNull(message = "Data da consulta é obrigatória")
    private LocalDateTime dataConsulta;
    
    @NotBlank(message = "Motivo da consulta é obrigatório")
    private String motivo;
    
    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;
    
    @NotNull(message = "ID do médico é obrigatório")
    private Long medicoId;
    
    private Long enfermeiroId;
    private String diagnostico;
    private String prescricao;
    private String observacoes;

    // Constructors
    public ConsultaInput() {}

    // Getters and Setters
    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public Long getEnfermeiroId() {
        return enfermeiroId;
    }

    public void setEnfermeiroId(Long enfermeiroId) {
        this.enfermeiroId = enfermeiroId;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
