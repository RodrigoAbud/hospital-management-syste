package com.fiap.atividade3.service;

import com.fiap.atividade3.model.entity.Consulta;
import com.fiap.atividade3.model.entity.Enfermeiro;
import com.fiap.atividade3.model.entity.Medico;
import com.fiap.atividade3.model.entity.Paciente;
import com.fiap.atividade3.repository.ConsultaRepository;
import com.fiap.atividade3.repository.EnfermeiroRepository;
import com.fiap.atividade3.repository.MedicoRepository;
import com.fiap.atividade3.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service to initialize database with sample data for testing
 */
@Service
public class DataInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (medicoRepository.count() == 0) {
            logger.info("Initializing database with sample data...");
            initializeData();
            logger.info("Database initialization completed.");
        } else {
            logger.info("Database already contains data. Skipping initialization.");
        }
    }

    private void initializeData() {
        // Create sample doctors
        Medico medico1 = new Medico("Dr. João Silva", "joao.silva@hospital.com", 
                                   passwordEncoder.encode("senha123"), "CRM12345", "Cardiologia");
        Medico medico2 = new Medico("Dra. Maria Santos", "maria.santos@hospital.com", 
                                   passwordEncoder.encode("senha123"), "CRM67890", "Pediatria");
        
        medicoRepository.save(medico1);
        medicoRepository.save(medico2);

        // Create sample nurses
        Enfermeiro enfermeiro1 = new Enfermeiro("Ana Costa", "ana.costa@hospital.com", 
                                               passwordEncoder.encode("senha123"), "COREN11111", "UTI");
        Enfermeiro enfermeiro2 = new Enfermeiro("Carlos Oliveira", "carlos.oliveira@hospital.com", 
                                               passwordEncoder.encode("senha123"), "COREN22222", "Emergência");
        
        enfermeiroRepository.save(enfermeiro1);
        enfermeiroRepository.save(enfermeiro2);

        // Create sample patients
        Paciente paciente1 = new Paciente("José Pereira", "jose.pereira@email.com", 
                                         passwordEncoder.encode("senha123"), "12345678901", 
                                         LocalDate.of(1980, 5, 15));
        paciente1.setTelefone("(11) 99999-1111");
        paciente1.setEndereco("Rua das Flores, 123 - São Paulo, SP");

        Paciente paciente2 = new Paciente("Maria Fernanda", "maria.fernanda@email.com", 
                                         passwordEncoder.encode("senha123"), "98765432109", 
                                         LocalDate.of(1992, 8, 22));
        paciente2.setTelefone("(11) 88888-2222");
        paciente2.setEndereco("Av. Paulista, 456 - São Paulo, SP");

        pacienteRepository.save(paciente1);
        pacienteRepository.save(paciente2);

        // Create sample consultations
        Consulta consulta1 = new Consulta(LocalDateTime.now().minusDays(2), 
                                         "Dor no peito e falta de ar", paciente1, medico1);
        consulta1.setEnfermeiro(enfermeiro1);
        consulta1.setDiagnostico("Possível arritmia cardíaca");
        consulta1.setPrescricao("Exames cardiológicos complementares");
        consulta1.setObservacoes("Paciente apresenta histórico familiar de problemas cardíacos");

        Consulta consulta2 = new Consulta(LocalDateTime.now().minusDays(1), 
                                         "Febre e dor de garganta", paciente2, medico2);
        consulta2.setEnfermeiro(enfermeiro2);
        consulta2.setDiagnostico("Faringite viral");
        consulta2.setPrescricao("Analgésicos e repouso");
        consulta2.setObservacoes("Sintomas leves, evolução favorável esperada");

        consultaRepository.save(consulta1);
        consultaRepository.save(consulta2);

        logger.info("Sample data created:");
        logger.info("- 2 Doctors (joao.silva@hospital.com, maria.santos@hospital.com)");
        logger.info("- 2 Nurses (ana.costa@hospital.com, carlos.oliveira@hospital.com)");
        logger.info("- 2 Patients (jose.pereira@email.com, maria.fernanda@email.com)");
        logger.info("- 2 Consultations");
        logger.info("Default password for all users: senha123");
    }
}
