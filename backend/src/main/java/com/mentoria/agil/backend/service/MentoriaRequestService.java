package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.dto.MentoriaRequestDTO;
import com.mentoria.agil.backend.exception.BusinessException;
import com.mentoria.agil.backend.model.MentoriaRequest;
import com.mentoria.agil.backend.model.MentoriaStatus;
import com.mentoria.agil.backend.model.Role;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.MentoriaRequestRepository;
import com.mentoria.agil.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.mentoria.agil.backend.interfaces.service.MentoriaRequestServiceInterface;

@Service
public class MentoriaRequestService implements MentoriaRequestServiceInterface {

    private final MentoriaRequestRepository requestRepository;
    private final UserRepository userRepository;

    public MentoriaRequestService(MentoriaRequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MentoriaRequest createRequest(User mentorado, MentoriaRequestDTO dto) {
        
        User mentor = userRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new EntityNotFoundException("Mentor não encontrado"));

        // Validação de mentor
        if (mentor.getRole() != Role.MENTOR) {
            throw new BusinessException("O usuário selecionado não é um mentor");
        }

        // Impedir auto-mentoria
        if (mentorado.getId().equals(mentor.getId())) {
            throw new BusinessException("Você não pode solicitar mentoria para si mesmo");
        }

        // Verificar duplicidade (solicitação pendente)
        boolean existsPending = requestRepository.existsByMentoradoAndMentorAndStatus(
                mentorado, mentor, MentoriaStatus.PENDING);
        if (existsPending) {
            throw new BusinessException("Já existe uma solicitação pendente para este mentor");
        }

        // Criação de nova solicitação
        MentoriaRequest request = new MentoriaRequest();
        request.setMentorado(mentorado);
        request.setMentor(mentor);
        request.setMessage(dto.getMessage());
        request.setStatus(MentoriaStatus.PENDING);

        return requestRepository.save(request);
    }
}
