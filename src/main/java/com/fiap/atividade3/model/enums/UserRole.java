package com.fiap.atividade3.model.enums;

/**
 * Enum representing the different user roles in the hospital management system
 */
public enum UserRole {
    MEDICO("ROLE_MEDICO", "Médico - pode visualizar e editar histórico de consultas"),
    ENFERMEIRO("ROLE_ENFERMEIRO", "Enfermeiro - pode registrar consultas e acessar histórico"),
    PACIENTE("ROLE_PACIENTE", "Paciente - pode visualizar apenas suas próprias consultas");

    private final String authority;
    private final String description;

    UserRole(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }
}
