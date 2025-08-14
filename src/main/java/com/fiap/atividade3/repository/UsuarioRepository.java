package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Usuario;
import com.fiap.atividade3.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Usuario entity operations
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Find user by email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Find users by role
     */
    List<Usuario> findByRole(UserRole role);

    /**
     * Find active users by role
     */
    List<Usuario> findByRoleAndActiveTrue(UserRole role);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find active users
     */
    List<Usuario> findByActiveTrue();

    /**
     * Custom query to find users by name containing (case insensitive)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND u.active = true")
    List<Usuario> findByNomeContainingIgnoreCaseAndActiveTrue(@Param("nome") String nome);
}
