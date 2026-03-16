package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.dto.response.HistoricoSessaoDTO;
import com.mentoria.agil.backend.interfaces.service.HistoricoMentoriaServiceInterface;
import com.mentoria.agil.backend.model.Material;
import com.mentoria.agil.backend.model.Sessao;
import com.mentoria.agil.backend.model.SessaoMaterial;
import com.mentoria.agil.backend.model.SessaoStatus;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.SessaoMaterialRepository;
import com.mentoria.agil.backend.repository.SessaoRepository;
import com.mentoria.agil.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoMentoriaService implements HistoricoMentoriaServiceInterface{

    private final SessaoRepository sessaoRepository;
    private final UserRepository userRepository;
    private final SessaoMaterialRepository sessaoMaterialRepository;

    public HistoricoMentoriaService(SessaoRepository sessaoRepository, UserRepository userRepository, 
                                            SessaoMaterialRepository sessaoMaterialRepository) {
        this.sessaoRepository = sessaoRepository;
        this.userRepository = userRepository;
        this.sessaoMaterialRepository = sessaoMaterialRepository;
    }

    @Override
    public List<HistoricoSessaoDTO> listarHistorico(User mentorado, Long mentorId) {
        List<Sessao> sessoes;

        if (mentorId != null) {
            User mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new EntityNotFoundException("Mentor não encontrado"));
            sessoes = sessaoRepository.findByMentoradoAndMentorAndStatusOrderByDataHoraInicioDesc(
                    mentorado, mentor, SessaoStatus.CONCLUIDA);
        } else {
            sessoes = sessaoRepository.findByMentoradoAndStatusOrderByDataHoraInicioDesc(
                    mentorado, SessaoStatus.CONCLUIDA);
        }

        return sessoes.stream()
                .map(sessao -> {
                    // Busca os materiais de apoio associados a uma sessão específica
                    List<Material> materiais = sessaoMaterialRepository.findBySessaoId(sessao.getId())
                            .stream()
                            .map(SessaoMaterial::getMaterial)
                            .collect(Collectors.toList());
                    return new HistoricoSessaoDTO(sessao, materiais);
                })
                .collect(Collectors.toList());
    }
}
