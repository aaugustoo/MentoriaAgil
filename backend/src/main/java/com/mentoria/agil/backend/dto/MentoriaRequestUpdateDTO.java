package com.mentoria.agil.backend.dto;

import com.mentoria.agil.backend.model.MentoriaStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MentoriaRequestUpdateDTO {
    @NotNull(message = "O status é obrigatório")
    private MentoriaStatus status; // ACCEPTED ou REJECTED

    @Size(max = 500, message = "A justificativa deve ter no máximo 500 caracteres")
    private String justificativa; // usado quando REJECTED

    public MentoriaStatus getStatus() { 
        return status; 
    }

    public void setStatus(MentoriaStatus status) { 
        this.status = status; 
    }

    public String getJustificativa() { 
        return justificativa; 
    }

    public void setJustificativa(String justificativa) { 
        this.justificativa = justificativa; 
    }
}