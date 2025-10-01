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

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPaciente(Paciente paciente);

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByMedico(Medico medico);

    List<Consulta> findByMedicoId(Long medicoId);

    @Query("SELECT c FROM Consulta c WHERE c.dataConsulta BETWEEN :startDate AND :endDate ORDER BY c.dataConsulta DESC")
    List<Consulta> findByDataConsultaBetween(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.dataConsulta BETWEEN :startDate AND :endDate ORDER BY c.dataConsulta DESC")
    List<Consulta> findByPacienteIdAndDataConsultaBetween(@Param("pacienteId") Long pacienteId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :medicoId AND c.dataConsulta BETWEEN :startDate AND :endDate ORDER BY c.dataConsulta DESC")
    List<Consulta> findByMedicoIdAndDataConsultaBetween(@Param("medicoId") Long medicoId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Consulta c WHERE c.dataConsulta >= :thirtyDaysAgo ORDER BY c.dataConsulta DESC")
    List<Consulta> findRecentConsultations(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    List<Consulta> findByPacienteIdOrderByDataConsultaDesc(Long pacienteId);

    List<Consulta> findByMedicoIdOrderByDataConsultaDesc(Long medicoId);

    List<Consulta> findByPacienteIdAndDataConsultaAfterOrderByDataConsultaAsc(Long pacienteId, LocalDateTime dataConsulta);

    @Query("SELECT c FROM Consulta c WHERE c.medico.especialidade = :especialidade ORDER BY c.dataConsulta DESC")
    List<Consulta> findByMedicoEspecialidadeOrderByDataConsultaDesc(@Param("especialidade") String especialidade);

    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.dataConsulta >= :dataConsulta")
    long countByDataConsultaAfter(@Param("dataConsulta") LocalDateTime dataConsulta);
}
