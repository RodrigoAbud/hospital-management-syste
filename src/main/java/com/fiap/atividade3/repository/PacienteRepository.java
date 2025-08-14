package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Paciente entity operations
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Find patient by CPF
     */
    Optional<Paciente> findByCpf(String cpf);

    /**
     * Find all active patients
     */
    @Query("SELECT p FROM Paciente p WHERE p.active = true")
    List<Paciente> findAllActiveTrue();

    /**
     * Check if CPF exists
     */
    boolean existsByCpf(String cpf);

    /**
     * Find patients by name containing (case insensitive)
     */
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND p.active = true")
    List<Paciente> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);

    /**
     * Find patients by phone
     */
    Optional<Paciente> findByTelefone(String telefone);
}
