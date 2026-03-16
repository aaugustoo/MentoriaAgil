package com.mentoria.agil.backend.controller;

import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.model.Material;
import com.mentoria.agil.backend.model.Sessao;
import com.mentoria.agil.backend.model.SessaoMaterial;
import com.mentoria.agil.backend.repository.MaterialRepository;
import com.mentoria.agil.backend.repository.SessaoMaterialRepository;
import com.mentoria.agil.backend.repository.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessoes")
public class SessaoMaterialController {

    private final SessaoRepository sessaoRepository;
    private final MaterialRepository materialRepository;
    private final SessaoMaterialRepository sessaoMaterialRepository;

    public SessaoMaterialController(SessaoRepository sessaoRepository,
                                     MaterialRepository materialRepository,
                                     SessaoMaterialRepository sessaoMaterialRepository) {
        this.sessaoRepository = sessaoRepository;
        this.materialRepository = materialRepository;
        this.sessaoMaterialRepository = sessaoMaterialRepository;
    }

    @PostMapping("/{sessaoId}/materiais/{materialId}")
    public ResponseEntity<Void> vincularMaterial(@PathVariable Long sessaoId, @PathVariable Long materialId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User mentor = (User) userDetails; // só mentor pode vincular um material de apoo a uma sessão

        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("Material não encontrado"));

        if (!sessao.getMentor().getId().equals(mentor.getId())) {
            throw new SecurityException("Apenas o mentor da sessão pode vincular materiais");
        }

        // Verifica se já existe vínculo
        boolean exists = sessaoMaterialRepository.findBySessaoId(sessaoId).stream()
                .anyMatch(sm -> sm.getMaterial().getId().equals(materialId));
        if (!exists) {
            sessaoMaterialRepository.save(new SessaoMaterial(sessao, material));
        }

        return ResponseEntity.ok().build();
    }
}
