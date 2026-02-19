package com.mentoria.agil.backend.dto.response;

import java.time.LocalDateTime;
import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;

public class PerfilMentorResponseDTO {
    
    private Long id;                  // id do perfil
    private Long userId;              // id do usuário
    private String name;              // User
    private String email;             // User
    private String role;              // User
    private String especializacao;    // PerfilMentor
    private String experiencias;      // PerfilMentor
    private String formacao;          // PerfilMentor
    private LocalDateTime createdAt;  // User

    // Construtor padrão
    public PerfilMentorResponseDTO() {}

    // Construtor que recebe as entidades
    public PerfilMentorResponseDTO(PerfilMentor perfil) {
        User user = perfil.getUser();
        this.id = perfil.getId();
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole() != null ? user.getRole().name() : null;     //conversão de role para string
        this.especializacao = perfil.getEspecializacao();
        this.experiencias = perfil.getExperiencias();
        this.formacao = perfil.getFormacao();
        this.createdAt = user.getCreatedAt();
    }

    // Construtor com todos os parâmetros
    public PerfilMentorResponseDTO(Long id, Long userId, String name, String email, 
                            String role, String especializacao, String experiencias, 
                            String formacao, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.especializacao = especializacao;
        this.experiencias = experiencias;
        this.formacao = formacao;
        this.createdAt = createdAt;
    }

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(String experiencias) {
        this.experiencias = experiencias;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
