package com.mentoria.agil.backend.controller;

import com.mentoria.agil.backend.dto.MentoriaRequestDTO;
import com.mentoria.agil.backend.dto.response.MentoriaResponseDTO;
import com.mentoria.agil.backend.model.MentoriaRequest;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.interfaces.service.MentoriaRequestServiceInterface;
import com.mentoria.agil.backend.dto.MentoriaRequestUpdateDTO;
import com.mentoria.agil.backend.dto.response.MentoriaRequestListResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorias")
public class MentoriaRequestController {
    private final MentoriaRequestServiceInterface requestService;

    public MentoriaRequestController(MentoriaRequestServiceInterface requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/request")
    public ResponseEntity<MentoriaResponseDTO> createRequest(@Valid @RequestBody MentoriaRequestDTO dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User mentorado = (User) userDetails;

        MentoriaRequest request = requestService.createRequest(mentorado, dto);
        return new ResponseEntity<>(new MentoriaResponseDTO(request), HttpStatus.CREATED);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<MentoriaRequestListResponseDTO>> listarPendentes() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User mentor = (User) userDetails;

        List<MentoriaRequestListResponseDTO> response = requestService.listarPendentes(mentor)
                .stream()
                .map(MentoriaRequestListResponseDTO::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MentoriaResponseDTO> atualizarStatus(@PathVariable Long id, @Valid @RequestBody MentoriaRequestUpdateDTO dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User mentor = (User) userDetails;

        MentoriaRequest request = requestService.atualizarStatus(id, mentor, dto);
        return ResponseEntity.ok(new MentoriaResponseDTO(request));
    }
}