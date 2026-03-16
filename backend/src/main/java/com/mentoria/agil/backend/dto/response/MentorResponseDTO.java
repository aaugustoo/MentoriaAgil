package com.mentoria.agil.backend.dto.response;

import com.mentoria.agil.backend.model.PerfilMentor;

public record MentorResponseDTO(
    String nome,
    String especializacao,
    String experienciaResumida,
    String areaPrincipal,
    String tipoMentoria,
    String disponibilidadeGeral
) {
    public MentorResponseDTO(PerfilMentor perfil) {
        this(
            perfil.getUser().getName(),
            perfil.getEspecializacao(),
            perfil.getExperiencias(), 
            perfil.getUser().getAreaInteresse(), 
            perfil.getUser().getTipoMentoria(),
            "Disponibilidade sob consulta" 
        );
    }
}