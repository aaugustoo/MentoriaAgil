package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.dto.MentoriaRequestDTO;
import com.mentoria.agil.backend.dto.MentoriaRequestUpdateDTO;
import com.mentoria.agil.backend.enums.MentoriaStatus;
import com.mentoria.agil.backend.enums.Role;
import com.mentoria.agil.backend.exception.BusinessException;
import com.mentoria.agil.backend.model.MentoriaRequest;
import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.MentoriaRequestRepository;
import com.mentoria.agil.backend.repository.PerfilMentorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentoriaRequestServiceTest {

    @Mock
    private MentoriaRequestRepository requestRepository;

    @Mock
    private PerfilMentorRepository perfilMentorRepository;

    @InjectMocks
    private MentoriaRequestService requestService;

    private User mentorado;
    private User mentor;
    private PerfilMentor perfil;
    private MentoriaRequestDTO dto;
    private MentoriaRequest requestPendente;
    private MentoriaRequest requestAceita;

    @BeforeEach
    void setUp() {
        mentorado = new User("João", "joao@email.com", "senha123");
        mentorado.setId(1L);
        mentorado.setRole(Role.USER);

        mentor = new User("Maria", "maria@email.com", "senha456");
        mentor.setId(2L);
        mentor.setRole(Role.MENTOR);

        perfil = new PerfilMentor();
        perfil.setId(2L);
        perfil.setUser(mentor);

        dto = new MentoriaRequestDTO();
        dto.setMentorId(2L);
        dto.setMessage("Quero ser mentorado por você.");

        requestPendente = new MentoriaRequest();
        requestPendente.setId(10L);
        requestPendente.setMentorado(mentorado);
        requestPendente.setMentor(mentor);
        requestPendente.setStatus(MentoriaStatus.PENDING);

        requestAceita = new MentoriaRequest();
        requestAceita.setId(20L);
        requestAceita.setMentorado(mentorado);
        requestAceita.setMentor(mentor);
        requestAceita.setStatus(MentoriaStatus.ACCEPTED);
    }

    @Test
    void deveLancarExcecaoQuandoMentorNaoEncontrado() {
        when(perfilMentorRepository.findById(99L)).thenReturn(Optional.empty());
        dto.setMentorId(99L);

        assertThrows(EntityNotFoundException.class, () -> requestService.createRequest(mentorado, dto));
    }

    @Test
    void deveLancarExcecaoQuandoAutoMentoria() {
        perfil.setUser(mentorado);
        when(perfilMentorRepository.findById(1L)).thenReturn(Optional.of(perfil));
        dto.setMentorId(1L);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> requestService.createRequest(mentorado, dto));
        assertEquals("Você não pode solicitar mentoria para si mesmo", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForMentor() {
        User usuarioComum = new User("Pedro", "pedro@email.com", "senha");
        usuarioComum.setId(3L);
        usuarioComum.setRole(Role.USER);

        PerfilMentor perfilInvalido = new PerfilMentor();
        perfilInvalido.setUser(usuarioComum);

        when(perfilMentorRepository.findById(3L)).thenReturn(Optional.of(perfilInvalido));
        dto.setMentorId(3L);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> requestService.createRequest(mentorado, dto));
        assertEquals("O usuário selecionado não é um mentor", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoSolicitacaoPendenteJaExiste() {
        when(perfilMentorRepository.findById(2L)).thenReturn(Optional.of(perfil));
        when(requestRepository.existsByMentoradoIdAndMentorIdAndStatus(anyLong(), anyLong(),
                eq(MentoriaStatus.PENDING)))
                .thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> requestService.createRequest(mentorado, dto));
        assertEquals("Já existe uma solicitação pendente para este mentor", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoMentorNaoTemPermissao() {
        User outroMentor = new User("Outro", "outro@email.com", "senha");
        outroMentor.setId(99L);

        when(requestRepository.findById(10L)).thenReturn(Optional.of(requestPendente));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> requestService.atualizarStatus(10L, outroMentor, new MentoriaRequestUpdateDTO()));
        assertEquals("Você não tem permissão para alterar esta solicitação", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoSolicitacaoJaProcessada() {
        when(requestRepository.findById(20L)).thenReturn(Optional.of(requestAceita));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> requestService.atualizarStatus(20L, mentor, new MentoriaRequestUpdateDTO()));
        assertEquals("Esta solicitação já foi processada", exception.getMessage());
    }

    @Test
    void deveCriarSolicitacaoComSucesso() {
        when(perfilMentorRepository.findById(2L)).thenReturn(Optional.of(perfil));
        when(requestRepository.existsByMentoradoIdAndMentorIdAndStatus(anyLong(), anyLong(), any())).thenReturn(false);
        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MentoriaRequest result = requestService.createRequest(mentorado, dto);

        assertNotNull(result);
        assertEquals(MentoriaStatus.PENDING, result.getStatus());
    }
}