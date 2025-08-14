package com.fiap.atividade3.model.entity;

import com.fiap.atividade3.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a patient in the hospital management system
 */
@Entity
@Table(name = "pacientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Paciente extends Usuario {

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    @Column(length = 15)
    private String telefone;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String endereco;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consulta> consultas = new ArrayList<>();

    // Constructors
    public Paciente() {
        super();
        setRole(UserRole.PACIENTE);
    }

    public Paciente(String nome, String email, String senha, String cpf, LocalDate dataNascimento) {
        super(nome, email, senha, UserRole.PACIENTE);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    // Getters and Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
