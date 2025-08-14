package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Medico entity operations
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * Find doctor by CRM
     */
    Optional<Medico> findByCrm(String crm);

    /**
     * Find doctors by specialty
     */
    List<Medico> findByEspecialidade(String especialidade);

    /**
     * Find active doctors by specialty
     */
    @Query("SELECT m FROM Medico m WHERE m.especialidade = :especialidade AND m.active = true")
    List<Medico> findByEspecialidadeAndActiveTrue(@Param("especialidade") String especialidade);

    /**
     * Find all active doctors
     */
    @Query("SELECT m FROM Medico m WHERE m.active = true")
    List<Medico> findAllActiveTrue();

    /**
     * Check if CRM exists
     */
    boolean existsByCrm(String crm);

    /**
     * Find doctors by name containing (case insensitive)
     */
    @Query("SELECT m FROM Medico m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND m.active = true")
    List<Medico> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);
}
