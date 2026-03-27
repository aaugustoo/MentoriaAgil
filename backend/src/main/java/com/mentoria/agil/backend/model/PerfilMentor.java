package com.mentoria.agil.backend.model;

import com.mentoria.agil.backend.enums.DisponibilidadeStatus;
import com.mentoria.agil.backend.enums.TipoMentoria;

import jakarta.persistence.*;

@Entity
@Table(name = "mentors")
public class PerfilMentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String especializacao;

    @Column(nullable = false, length = 1000)
    private String experiencias;

    private String formacao;

    private String areaPrincipal;

    @Enumerated(EnumType.STRING)
    private TipoMentoria tipoMentoria;

    @Enumerated(EnumType.STRING)
    private DisponibilidadeStatus disponibilidade;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    public PerfilMentor() {
    }

    public PerfilMentor(String especializacao, String experiencias, String formacao,
            String areaPrincipal, TipoMentoria tipo, DisponibilidadeStatus disp, User user) {
        this.especializacao = especializacao;
        this.experiencias = experiencias;
        this.formacao = formacao;
        this.areaPrincipal = areaPrincipal;
        this.tipoMentoria = tipo;
        this.disponibilidade = disp;
        this.user = user;
    }

    // Getters e Setters
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

    public String getAreaPrincipal() {
        return areaPrincipal;
    }

    public void setAreaPrincipal(String areaPrincipal) {
        this.areaPrincipal = areaPrincipal;
    }

    public TipoMentoria getTipoMentoria() {
        return tipoMentoria;
    }

    public void setTipoMentoria(TipoMentoria tipoMentoria) {
        this.tipoMentoria = tipoMentoria;
    }

    public DisponibilidadeStatus getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(DisponibilidadeStatus disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}