package com.fiap.atividade3.controller;

import com.fiap.atividade3.graphql.input.ConsultaInput;
import com.fiap.atividade3.graphql.input.ConsultaUpdateInput;
import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Usuario;

import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * REST Controller for consultation operations
 */
@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas médicas")
@SecurityRequirement(name = "bearerAuth")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;



    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    /**
     * Get consultation by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna uma consulta específica por ID")
    public ResponseEntity<Consulta> getConsulta(@PathVariable Long id) {
        try {
            Usuario usuario = getCurrentUser();
            Consulta consulta = consultaService.buscarConsultaPorId(id, usuario);
            return ResponseEntity.ok(consulta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get current user's consultations (for patients)
     */
    @GetMapping("/minhas")
    @Operation(summary = "Minhas consultas", description = "Retorna as consultas do usuário autenticado (para pacientes)")
    public ResponseEntity<List<Consulta>> getMinhasConsultas() {
        try {
            Usuario usuario = getCurrentUser();
            List<Consulta> consultas = consultaService.buscarConsultasPorPaciente(usuario.getId(), usuario);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get consultations by patient ID
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Consultas por paciente", description = "Retorna consultas de um paciente específico")
    public ResponseEntity<List<Consulta>> getConsultasPorPaciente(@PathVariable Long pacienteId) {
        try {
            Usuario usuario = getCurrentUser();
            List<Consulta> consultas = consultaService.buscarConsultasPorPaciente(pacienteId, usuario);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get consultations by doctor ID
     */
    @GetMapping("/medico/{medicoId}")
    @Operation(summary = "Consultas por médico", description = "Retorna consultas de um médico específico")
    public ResponseEntity<List<Consulta>> getConsultasPorMedico(@PathVariable Long medicoId) {
        try {
            List<Consulta> consultas = consultaService.buscarConsultasPorMedico(medicoId);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all consultations (medical staff only)
     */
    @GetMapping("/todas")
    @Operation(summary = "Todas as consultas", description = "Retorna todas as consultas (apenas para equipe médica)")
    public ResponseEntity<List<Consulta>> getTodasConsultas() {
        try {
            List<Consulta> consultas = consultaService.buscarTodasConsultas();
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get recent consultations (last 30 days)
     */
    @GetMapping("/recentes")
    @Operation(summary = "Consultas recentes", description = "Retorna consultas dos últimos 30 dias")
    public ResponseEntity<List<Consulta>> getConsultasRecentes() {
        try {
            List<Consulta> consultas = consultaService.buscarConsultasRecentes();
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create new consultation
     */
    @PostMapping
    @Operation(summary = "Criar consulta", description = "Cria uma nova consulta médica")
    public ResponseEntity<Consulta> criarConsulta(@RequestBody ConsultaInput input) {
        try {
            Consulta consulta = new Consulta();
            consulta.setDataConsulta(input.getDataConsulta());
            consulta.setMotivo(input.getMotivo());
            consulta.setDiagnostico(input.getDiagnostico());
            consulta.setPrescricao(input.getPrescricao());
            consulta.setObservacoes(input.getObservacoes());

            // Set relationships
            consulta.setPaciente(pacienteRepository.findById(input.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado")));
            consulta.setMedico(medicoRepository.findById(input.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("Médico não encontrado")));
            
            if (input.getEnfermeiroId() != null) {
                consulta.setEnfermeiro(enfermeiroRepository.findById(input.getEnfermeiroId())
                        .orElseThrow(() -> new RuntimeException("Enfermeiro não encontrado")));
            }

            Consulta savedConsulta = consultaService.criarConsulta(consulta);
            return ResponseEntity.ok(savedConsulta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update consultation
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar consulta", description = "Atualiza uma consulta existente (apenas médicos)")
    public ResponseEntity<Consulta> atualizarConsulta(@PathVariable Long id, @RequestBody ConsultaUpdateInput input) {
        try {
            Consulta consultaAtualizada = new Consulta();
            consultaAtualizada.setDiagnostico(input.getDiagnostico());
            consultaAtualizada.setPrescricao(input.getPrescricao());
            consultaAtualizada.setObservacoes(input.getObservacoes());

            Consulta consulta = consultaService.atualizarConsulta(id, consultaAtualizada);
            return ResponseEntity.ok(consulta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete consultation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar consulta", description = "Remove uma consulta (apenas médicos)")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        try {
            consultaService.deletarConsulta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuário não autenticado");
    }
}
