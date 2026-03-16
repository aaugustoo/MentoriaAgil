package com.mentoria.agil.backend.dto.response;

import com.mentoria.agil.backend.model.Material;
import com.mentoria.agil.backend.model.TipoMaterial;
import java.time.LocalDateTime;

public class MaterialResponseDTO {
    
    private Long id;
    private String titulo;
    private String descricao;
    private TipoMaterial tipo;
    private String conteudo;
    private Long mentorId;
    private String mentorNome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MaterialResponseDTO(){}

    public MaterialResponseDTO (Material material) {
        this.id = material.getId();
        this.titulo = material.getTitulo();
        this.descricao = material.getDescricao();
        this.tipo = material.getTipo();
        this.conteudo = material.getConteudo();
        this.mentorId = material.getMentor().getId();
        this.mentorNome = material.getMentor().getName();
        this.createdAt = material.getCreatedAt();
        this.updatedAt = material.getUpdatedAt();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getMentorNome() {
        return mentorNome;
    }

    public void setMentorNome(String mentorNome) {
        this.mentorNome = mentorNome;
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
}
