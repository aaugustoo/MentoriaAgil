package com.mentoria.agil.backend.dto;

import jakarta.validation.constraints.*;

public class FeedbackRequestDTO {

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres")
    private String comentario;

    public FeedbackRequestDTO() {}

    public FeedbackRequestDTO(Integer nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
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
}
