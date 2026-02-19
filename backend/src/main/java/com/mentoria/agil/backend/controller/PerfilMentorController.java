package com.mentoria.agil.backend.controller;

import com.mentoria.agil.backend.dto.PerfilMentorRequestDTO;
import com.mentoria.agil.backend.dto.response.PerfilMentorResponseDTO;
import com.mentoria.agil.backend.interfaces.service.PerfilMentorServiceInterface;
import com.mentoria.agil.backend.dto.response.PerfilMentorListResponseDTO;
import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentors")
public class PerfilMentorController {
    
    private final PerfilMentorServiceInterface mentorService;

    public PerfilMentorController(PerfilMentorServiceInterface mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping
    public ResponseEntity<PerfilMentorResponseDTO> criarPerfilMentor(@Valid @RequestBody PerfilMentorRequestDTO request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        User userAutenticado = (User) userDetails;
        
        // Cria o perfil de mentor usando o serviço
        PerfilMentor perfilSalvo = mentorService.criarPerfilMentor(
            userAutenticado,
            request.getEspecializacao(),
            request.getExperiencias(),
            request.getFormacao()
        );
        
        // Converte para DTO e retorna
        PerfilMentorResponseDTO response = new PerfilMentorResponseDTO(perfilSalvo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PerfilMentorListResponseDTO>> listarMentores() {
        List<PerfilMentor> mentores = mentorService.listarTodos();
        
        List<PerfilMentorListResponseDTO> response = mentores.stream()
            .map(PerfilMentorListResponseDTO::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilMentorResponseDTO> buscarMentor(@PathVariable Long id) {
        PerfilMentor perfil = mentorService.buscarPorId(id);
        PerfilMentorResponseDTO response = new PerfilMentorResponseDTO(perfil);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilMentorResponseDTO> atualizarMentor(
            @PathVariable Long id, 
            @Valid @RequestBody PerfilMentorRequestDTO request) {
        
        PerfilMentor perfil = mentorService.buscarPorId(id);
        User user = perfil.getUser();
        
        // Verifica se o usuário autenticado é o dono do perfil
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        if (!user.getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Atualiza dados
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // password tem endpoint separado
        
        perfil.setEspecializacao(request.getEspecializacao());
        perfil.setExperiencias(request.getExperiencias());
        perfil.setFormacao(request.getFormacao());
        
        // Salva
        PerfilMentor perfilAtualizado = mentorService.atualizar(user, perfil);
        PerfilMentorResponseDTO response = new PerfilMentorResponseDTO(perfilAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMentor(@PathVariable Long id) {
        // Verifica se o usuário autenticado é o dono do perfil ou ADMIN
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        PerfilMentor perfil = mentorService.buscarPorId(id);
        
        boolean isAdmin = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !perfil.getUser().getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        mentorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
