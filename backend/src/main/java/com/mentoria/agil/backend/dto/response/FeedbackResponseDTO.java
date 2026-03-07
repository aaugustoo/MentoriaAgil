package com.mentoria.agil.backend.dto.response;

import com.mentoria.agil.backend.model.Feedback;
import java.time.LocalDateTime;

public class FeedbackResponseDTO {

    private Long id;
    private Long sessaoId;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataCriacao;

    public FeedbackResponseDTO() {}

    public FeedbackResponseDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.sessaoId = feedback.getSessao().getId();
        this.nota = feedback.getNota();
        this.comentario = feedback.getComentario();
        this.dataCriacao = feedback.getDataCriacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(Long sessaoId) {
        this.sessaoId = sessaoId;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
