package com.fiap.atividade3.graphql.input;

/**
 * Input type for updating consultations
 */
public class ConsultaUpdateInput {
    
    private String diagnostico;
    private String prescricao;
    private String observacoes;

    // Constructors
    public ConsultaUpdateInput() {}

    public ConsultaUpdateInput(String diagnostico, String prescricao, String observacoes) {
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.observacoes = observacoes;
    }

    // Getters and Setters
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
