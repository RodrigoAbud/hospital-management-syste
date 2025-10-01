package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {


    Optional<Medico> findByCrm(String crm);


    List<Medico> findByEspecialidade(String especialidade);


    @Query("SELECT m FROM Medico m WHERE m.especialidade = :especialidade AND m.active = true")
    List<Medico> findByEspecialidadeAndActiveTrue(@Param("especialidade") String especialidade);


    @Query("SELECT m FROM Medico m WHERE m.active = true")
    List<Medico> findAllActiveTrue();


    boolean existsByCrm(String crm);


    @Query("SELECT m FROM Medico m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND m.active = true")
    List<Medico> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);
}
