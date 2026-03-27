package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.dto.MentoriaRequestDTO;
import com.mentoria.agil.backend.dto.MentoriaRequestUpdateDTO;
import com.mentoria.agil.backend.enums.MentoriaStatus;
import com.mentoria.agil.backend.enums.Role;
import com.mentoria.agil.backend.exception.BusinessException;
import com.mentoria.agil.backend.interfaces.service.MentoriaRequestServiceInterface;
import com.mentoria.agil.backend.model.MentoriaRequest;
import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.MentoriaRequestRepository;
import com.mentoria.agil.backend.repository.PerfilMentorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MentoriaRequestService implements MentoriaRequestServiceInterface {

    private final MentoriaRequestRepository requestRepository;
    private final PerfilMentorRepository perfilMentorRepository;

    public MentoriaRequestService(MentoriaRequestRepository requestRepository,
            PerfilMentorRepository perfilMentorRepository) {
        this.requestRepository = requestRepository;
        this.perfilMentorRepository = perfilMentorRepository;
    }

    @Override
    @Transactional
    public MentoriaRequest createRequest(User mentorado, MentoriaRequestDTO dto) {
        PerfilMentor perfil = perfilMentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de mentor não encontrado"));

        User mentor = perfil.getUser();

        // 1. Validar identidade primeiro
        if (mentorado.getId().equals(mentor.getId())) {
            throw new BusinessException("Você não pode solicitar mentoria para si mesmo");
        }

        // 2. Validar cargo
        if (mentor.getRole() != Role.MENTOR) {
            throw new BusinessException("O usuário selecionado não é um mentor");
        }

        if (requestRepository.existsByMentoradoIdAndMentorIdAndStatus(mentorado.getId(), mentor.getId(),
                MentoriaStatus.PENDING)) {
            throw new BusinessException("Já existe uma solicitação pendente para este mentor");
        }

        MentoriaRequest request = new MentoriaRequest();
        request.setMentorado(mentorado);
        request.setMentor(mentor);
        request.setMessage(dto.getMessage());
        request.setStatus(MentoriaStatus.PENDING);
        request.setDataHoraProposta(dto.getDataHoraProposta());
        request.setFormato(dto.getFormato());
        request.setLinkReuniao(dto.getLinkReuniao());
        request.setEndereco(dto.getEndereco());

        return requestRepository.save(request);
    }

    @Override
    public List<MentoriaRequest> listarPendentes(User mentor) {
        return requestRepository.findByMentorIdAndStatusOrderByCreatedAtDesc(mentor.getId(), MentoriaStatus.PENDING);
    }

    @Override
    @Transactional
    public MentoriaRequest atualizarStatus(Long requestId, User mentor, MentoriaRequestUpdateDTO dto) {
        MentoriaRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (!request.getMentor().getId().equals(mentor.getId())) {
            throw new BusinessException("Você não tem permissão para alterar esta solicitação");
        }

        if (request.getStatus() != MentoriaStatus.PENDING) {
            throw new BusinessException("Esta solicitação já foi processada");
        }

        request.setStatus(dto.getStatus());
        if (dto.getStatus() == MentoriaStatus.REJECTED) {
            request.setJustificativaRecusa(dto.getJustificativa());
        }
        return requestRepository.save(request);
    }
}