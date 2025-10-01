package com.fiap.atividade3.model.entity;

import com.fiap.atividade3.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enfermeiros")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Enfermeiro extends Usuario {

    @NotBlank(message = "COREN é obrigatório")
    @Size(min = 4, max = 20, message = "COREN deve ter entre 4 e 20 caracteres")
    @Column(unique = true, nullable = false, length = 20)
    private String coren;

    @Size(max = 100, message = "Setor deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String setor;

    @OneToMany(mappedBy = "enfermeiro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consulta> consultasRegistradas = new ArrayList<>();

    // Constructors
    public Enfermeiro() {
        super();
        setRole(UserRole.ENFERMEIRO);
    }

    public Enfermeiro(String nome, String email, String senha, String coren, String setor) {
        super(nome, email, senha, UserRole.ENFERMEIRO);
        this.coren = coren;
        this.setor = setor;
    }

    // Getters and Setters
    public String getCoren() {
        return coren;
    }

    public void setCoren(String coren) {
        this.coren = coren;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public List<Consulta> getConsultasRegistradas() {
        return consultasRegistradas;
    }

    public void setConsultasRegistradas(List<Consulta> consultasRegistradas) {
        this.consultasRegistradas = consultasRegistradas;
    }
}
