package com.mentoria.agil.backend.controller;

import com.mentoria.agil.backend.dto.AgendamentoRequestDTO;
import com.mentoria.agil.backend.dto.response.SessaoResponseDTO;
import com.mentoria.agil.backend.enums.SessaoStatus;
import com.mentoria.agil.backend.interfaces.service.AgendamentoServiceInterface;
import com.mentoria.agil.backend.model.Sessao;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessoes")
public class AgendamentoController {

    private final AgendamentoServiceInterface agendamentoService;
    private final UserRepository userRepository;

    public AgendamentoController(AgendamentoServiceInterface agendamentoService, UserRepository userRepository) {
        this.agendamentoService = agendamentoService;
        this.userRepository = userRepository;
    }

    @PostMapping("/agendar")
    public ResponseEntity<SessaoResponseDTO> agendar(@Valid @RequestBody AgendamentoRequestDTO dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca o usuário de forma segura para evitar erros de cast
        User mentorado = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado"));

        Sessao sessao = agendamentoService.agendar(mentorado, dto);
        return new ResponseEntity<>(new SessaoResponseDTO(sessao), HttpStatus.CREATED);
    }

    @GetMapping("/mentor/pendentes")
    public ResponseEntity<List<SessaoResponseDTO>> listarSessoesPendentes() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User mentor = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Mentor não encontrado"));


        List<SessaoResponseDTO> sessoes = agendamentoService.buscarSessoesPorMentor(mentor);
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/minhas-sessoes")
    public ResponseEntity<List<SessaoResponseDTO>> listarMinhasSessoes(
            @RequestParam(required = false, defaultValue = "AGENDADA") SessaoStatus status) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado"));

        List<Sessao> sessoes = agendamentoService.listarSessoesPorUsuario(user, status);

        List<SessaoResponseDTO> response = sessoes.stream()
                .map(SessaoResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
