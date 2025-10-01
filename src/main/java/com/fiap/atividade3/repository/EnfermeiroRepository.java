package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Enfermeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EnfermeiroRepository extends JpaRepository<Enfermeiro, Long> {


    Optional<Enfermeiro> findByCoren(String coren);

    List<Enfermeiro> findBySetor(String setor);

    @Query("SELECT e FROM Enfermeiro e WHERE e.setor = :setor AND e.active = true")
    List<Enfermeiro> findBySetorAndActiveTrue(@Param("setor") String setor);

    @Query("SELECT e FROM Enfermeiro e WHERE e.active = true")
    List<Enfermeiro> findAllActiveTrue();

    boolean existsByCoren(String coren);

    @Query("SELECT e FROM Enfermeiro e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND e.active = true")
    List<Enfermeiro> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);
}
