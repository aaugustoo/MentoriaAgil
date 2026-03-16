package com.mentoria.agil.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MentoriaRequestDTO {
    @NotNull(message = "ID do mentor é obrigatório")
    private Long mentorId;

    @NotBlank(message = "Mensagem é obrigatória")
    private String message;

    public Long getMentorId(){
        return this.mentorId;
    }

    public void setMentorId(Long mentorId){
        this.mentorId = mentorId;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
