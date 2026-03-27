package com.mentoria.agil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.mentoria.agil.backend.enums.MentoriaStatus;
import com.mentoria.agil.backend.enums.FormatoSessao;

@Entity
@Table(name = "mentorship_requests")
public class MentoriaRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentorado_id", nullable = false)
    private User mentorado;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentoriaStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String justificativaRecusa;

    // --- NOVOS CAMPOS ADICIONADOS ---
    @Column(name = "data_hora_proposta")
    private LocalDateTime dataHoraProposta;

    @Enumerated(EnumType.STRING)
    private FormatoSessao formato;

    @Column(name = "link_reuniao")
    private String linkReuniao;

    @Column(name = "endereco")
    private String endereco;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getMentorado() {
        return mentorado;
    }

    public void setMentorado(User mentorado) {
        this.mentorado = mentorado;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MentoriaStatus getStatus() {
        return status;
    }

    public void setStatus(MentoriaStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getJustificativaRecusa() {
        return justificativaRecusa;
    }

    public void setJustificativaRecusa(String justificativaRecusa) {
        this.justificativaRecusa = justificativaRecusa;
    }

    public LocalDateTime getDataHoraProposta() {
        return dataHoraProposta;
    }

    public void setDataHoraProposta(LocalDateTime dataHoraProposta) {
        this.dataHoraProposta = dataHoraProposta;
    }

    public FormatoSessao getFormato() {
        return formato;
    }

    public void setFormato(FormatoSessao formato) {
        this.formato = formato;
    }

    public String getLinkReuniao() {
        return linkReuniao;
    }

    public void setLinkReuniao(String linkReuniao) {
        this.linkReuniao = linkReuniao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}