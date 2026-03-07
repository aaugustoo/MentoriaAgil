package com.mentoria.agil.backend.interfaces.service;

import com.mentoria.agil.backend.dto.MentoriaRequestDTO;
import com.mentoria.agil.backend.model.MentoriaRequest;
import com.mentoria.agil.backend.model.User;

public interface MentoriaRequestServiceInterface {
    public MentoriaRequest createRequest(User mentee, MentoriaRequestDTO dto);
}