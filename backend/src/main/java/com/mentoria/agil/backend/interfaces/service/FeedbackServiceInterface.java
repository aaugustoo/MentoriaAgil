package com.mentoria.agil.backend.interfaces.service;

import com.mentoria.agil.backend.dto.FeedbackRequestDTO;
import com.mentoria.agil.backend.model.Feedback;
import com.mentoria.agil.backend.model.User;

public interface FeedbackServiceInterface {

    public Feedback criarFeedback(Long sessaoId, User mentorado, FeedbackRequestDTO dto);
    public Double calcularMediaMentor(User mentor);
    
}
