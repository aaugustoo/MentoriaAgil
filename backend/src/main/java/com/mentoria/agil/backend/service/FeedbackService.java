package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.dto.FeedbackRequestDTO;
import com.mentoria.agil.backend.exception.BusinessException;
import com.mentoria.agil.backend.interfaces.service.FeedbackServiceInterface;
import com.mentoria.agil.backend.model.Feedback;
import com.mentoria.agil.backend.model.Sessao;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.FeedbackRepository;
import com.mentoria.agil.backend.repository.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackService implements FeedbackServiceInterface{

    private final SessaoRepository sessaoRepository;
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(SessaoRepository sessaoRepository, FeedbackRepository feedbackRepository) {
        this.sessaoRepository = sessaoRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional
    public Feedback criarFeedback(Long sessaoId, User mentorado, FeedbackRequestDTO dto) {

        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        // Verifica se o mentorado é o participante da sessão
        if (!sessao.getMentorado().getId().equals(mentorado.getId())) {
            throw new BusinessException("Você não tem permissão para avaliar esta sessão");
        }

        // Verifica se a sessão já foi concluída (ou se já passou do horário de término)
        if (sessao.getDataHoraFim().isAfter(LocalDateTime.now())) {
            throw new BusinessException("A sessão ainda não terminou. A avaliação só pode ser feita após o término.");
        }

        // Verifica se já existe feedback para esta sessão
        if (feedbackRepository.existsBySessaoId(sessaoId)) {
            throw new BusinessException("Esta sessão já foi avaliada");
        }

        // Cria e salva feedback
        Feedback feedback = new Feedback();
        feedback.setSessao(sessao);
        feedback.setNota(dto.getNota());
        feedback.setComentario(dto.getComentario());

        return feedbackRepository.save(feedback);
    }

    // Método para calcular a média do mentor
    public Double calcularMediaMentor(User mentor) {
        Double media = feedbackRepository.calcularMediaNotasPorMentor(mentor);
        return media != null ? media : 0.0;
    }
}
