package com.mentoria.agil.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.mentoria.agil.backend.enums.FormatoSessao;

public class AgendamentoRequestDTO {

    @NotNull
    private Long mentorId;
    @NotNull
    @Future
    private LocalDateTime dataHoraInicio;
    @NotNull
    @Future
    private LocalDateTime dataHoraFim;
    @NotNull
    private FormatoSessao formato;
    private String linkReuniao;
    private String endereco;

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
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