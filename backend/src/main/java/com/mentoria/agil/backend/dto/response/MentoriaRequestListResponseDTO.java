package com.mentoria.agil.backend.dto.response;

import com.mentoria.agil.backend.enums.MentoriaStatus;
import com.mentoria.agil.backend.model.MentoriaRequest;
import java.time.LocalDateTime;

public class MentoriaRequestListResponseDTO {
    private Long id;
    private Long mentoradoId;
    private String mentoradoNome;
    private String mensagem;
    private MentoriaStatus status;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataHoraProposta;
    private String formato;
    private String linkReuniao;
    private String endereco;

    public MentoriaRequestListResponseDTO(MentoriaRequest request) {
        this.id = request.getId();
        this.mentoradoNome = request.getMentorado().getName();
        this.mensagem = request.getMessage();
        this.dataHoraProposta = request.getDataHoraProposta();
        this.formato = request.getFormato() != null ? request.getFormato().toString() : null;
        this.linkReuniao = request.getLinkReuniao();
        this.endereco = request.getEndereco();
        this.status = request.getStatus();
        this.dataSolicitacao = request.getCreatedAt();
        this.mentoradoId = request.getMentorado().getId();
    }

    public Long getId() {
        return id;
    }

    public Long getMentoradoId() {
        return mentoradoId;
    }

    public String getMentoradoNome() {
        return mentoradoNome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public MentoriaStatus getStatus() {
        return status;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDateTime getDataHoraProposta() {
        return dataHoraProposta;
    }

    public String getFormato() {
        return formato;
    }

    public String getLinkReuniao() {
        return linkReuniao;
    }

    public String getEndereco() {
        return endereco;
    }
}