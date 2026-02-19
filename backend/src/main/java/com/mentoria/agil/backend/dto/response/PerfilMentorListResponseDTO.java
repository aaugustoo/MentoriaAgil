package com.mentoria.agil.backend.dto.response;

import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;

// ==== versão resumida para listagens === //

public class PerfilMentorListResponseDTO {
    private Long id;                  // id do perfil
    private String name;              // User
    private String email;             // User
    private String especializacao;    // PerfilMentor
    private String formacao;          // PerfilMentor

    // Construtor padrão
    public PerfilMentorListResponseDTO() {}

    // Construtor que recebe as entidades
    public PerfilMentorListResponseDTO(PerfilMentor perfil) {
        User user = perfil.getUser();
        this.id = perfil.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.especializacao = perfil.getEspecializacao();
        this.formacao = perfil.getFormacao();
    }

    // Construtor com parâmetros
    public PerfilMentorListResponseDTO(Long id, String name, String email, 
                                String especializacao, String formacao) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.especializacao = especializacao;
        this.formacao = formacao;
    }

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
}
