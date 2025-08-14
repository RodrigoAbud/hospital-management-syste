package com.fiap.atividade3.model.entity;

import com.fiap.atividade3.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a doctor in the hospital management system
 */
@Entity
@Table(name = "medicos")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Medico extends Usuario {

    @NotBlank(message = "CRM é obrigatório")
    @Size(min = 4, max = 20, message = "CRM deve ter entre 4 e 20 caracteres")
    @Column(unique = true, nullable = false, length = 20)
    private String crm;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(max = 100, message = "Especialidade deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String especialidade;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consulta> consultas = new ArrayList<>();

    // Constructors
    public Medico() {
        super();
        setRole(UserRole.MEDICO);
    }

    public Medico(String nome, String email, String senha, String crm, String especialidade) {
        super(nome, email, senha, UserRole.MEDICO);
        this.crm = crm;
        this.especialidade = especialidade;
    }

    // Getters and Setters
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
