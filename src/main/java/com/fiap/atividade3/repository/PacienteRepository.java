package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {


    Optional<Paciente> findByCpf(String cpf);


    @Query("SELECT p FROM Paciente p WHERE p.active = true")
    List<Paciente> findAllActiveTrue();


    boolean existsByCpf(String cpf);


    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND p.active = true")
    List<Paciente> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);


    Optional<Paciente> findByTelefone(String telefone);
}
