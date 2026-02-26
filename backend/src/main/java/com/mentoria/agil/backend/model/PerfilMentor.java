package com.mentoria.agil.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentors")
public class PerfilMentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String especializacao;

    @Column(nullable = false)
    private String experiencias;

    @Column(nullable = true)
    private String formacao;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private User user;

    public PerfilMentor() {}

    public PerfilMentor(String especializacao, String experiencias, User user) {
        this.especializacao = especializacao;
        this.experiencias = experiencias;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return user != null ? user.getName() : null;
    }
    
    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }
}