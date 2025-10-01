package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);


    List<Usuario> findByRole(UserRole role);


    List<Usuario> findByRoleAndActiveTrue(UserRole role);


    boolean existsByEmail(String email);


    List<Usuario> findByActiveTrue();


    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND u.active = true")
    List<Usuario> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);
}
