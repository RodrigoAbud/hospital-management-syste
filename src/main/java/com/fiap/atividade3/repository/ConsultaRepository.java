package com.fiap.atividade3.repository;

import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Consulta entity operations
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * Find consultations by patient
     */
    List<Consulta> findByPaciente(Paciente paciente);

    /**
     * Find consultations by patient ID
     */
    List<Consulta> findByPacienteId(Long pacienteId);

    /**
     * Find consultations by doctor
     */
    List<Consulta> findByMedico(Medico medico);

    /**
     * Find consultations by doctor ID
     */
    List<Consulta> findByMedicoId(Long medicoId);

    /**
     * Find consultations by date range
     */
    @Query("SELECT c FROM Consulta c WHERE c.dataConsulta BETWEEN :startDate AND :endDate ORDER BY c.dataConsulta DESC")
    List<Consulta> findByDataConsultaBetween(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Find consultations by patient and date range
     */
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.dataConsulta BETWEEN :startDate AND :endDate ORDER BY c.dataConsulta DESC")
    List<Consulta> findByPacienteIdAndDataConsultaBetween(@Param("pacienteId") Long pacienteId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Find consultations by doctor and date range
     */
    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :medicoId AND c.dataConsulta BETWEEN :startDate AND :endDate ORDER BY c.dataConsulta DESC")
    List<Consulta> findByMedicoIdAndDataConsultaBetween(@Param("medicoId") Long medicoId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent consultations (last 30 days)
     */
    @Query("SELECT c FROM Consulta c WHERE c.dataConsulta >= :thirtyDaysAgo ORDER BY c.dataConsulta DESC")
    List<Consulta> findRecentConsultations(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    /**
     * Find consultations by patient ordered by date descending
     */
    List<Consulta> findByPacienteIdOrderByDataConsultaDesc(Long pacienteId);

    /**
     * Find consultations by doctor ordered by date descending
     */
    List<Consulta> findByMedicoIdOrderByDataConsultaDesc(Long medicoId);
}
