package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.dto.AgendamentoRequestDTO;
import com.mentoria.agil.backend.enums.FormatoSessao;
import com.mentoria.agil.backend.enums.MentoriaStatus;
import com.mentoria.agil.backend.enums.Role;
import com.mentoria.agil.backend.enums.SessaoStatus;
import com.mentoria.agil.backend.exception.BusinessException;
import com.mentoria.agil.backend.model.*;
import com.mentoria.agil.backend.repository.DisponibilidadeRepository;
import com.mentoria.agil.backend.repository.MentoriaRequestRepository;
import com.mentoria.agil.backend.repository.SessaoRepository;
import com.mentoria.agil.backend.repository.UserRepository;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DisponibilidadeRepository disponibilidadeRepository;

    @Mock
    private MentoriaRequestRepository requestRepository;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private User mentor;
    private User mentorado;
    private AgendamentoRequestDTO dto;

    @BeforeEach
    void setUp() {
        mentor = new User("Mentor", "mentor@test.com", "123");
        mentor.setId(1L);
        mentor.setRole(Role.MENTOR);

        mentorado = new User("Aluno", "aluno@test.com", "123");
        mentorado.setId(2L);
        mentorado.setRole(Role.USER);

        dto = new AgendamentoRequestDTO();
        dto.setMentorId(1L);
        dto.setDataHoraInicio(LocalDateTime.now().plusDays(1));
        dto.setDataHoraFim(LocalDateTime.now().plusDays(1).plusHours(1));
        dto.setFormato(FormatoSessao.ONLINE);
        dto.setLinkReuniao("http://reuniao.com");
    }

    @Test
    void deveAgendarComSucessoEVincularRequestPendente() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mentor));
        when(disponibilidadeRepository.existsByMentorIdAndHorario(anyLong(), any(), any())).thenReturn(true);
        when(sessaoRepository.existsConflito(anyLong(), any(), any())).thenReturn(false);
        when(requestRepository.findByMentoradoIdAndMentorIdAndStatus(
                anyLong(), anyLong(), any(MentoriaStatus.class)))
                .thenReturn(Collections.emptyList());
        when(requestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sessaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Sessao s = agendamentoService.agendar(mentorado, dto);

        assertNotNull(s);
        verify(requestRepository).save(any(MentoriaRequest.class));
    }

    @Test
    void deveLancarExcecaoQuandoOnlineSemLink() {
        dto.setLinkReuniao("");
        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));
        assertEquals("Link da reunião é obrigatório para sessões online", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoMentorNaoEncontrado() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> agendamentoService.agendar(mentorado, dto));
    }

    @Test
    void deveLancarExcecaoQuandoDataInicioNoPassado() {
        dto.setDataHoraInicio(LocalDateTime.now().minusDays(1));
        assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));
    }

    @Test
    void deveLancarExcecaoQuandoPresencialSemEndereco() {
        dto.setFormato(FormatoSessao.PRESENCIAL);
        dto.setEndereco(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));

        assertEquals("Endereço é obrigatório para sessões presenciais", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForMentor() {
        mentor.setRole(Role.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mentor));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));

        assertEquals("O usuário selecionado não é um mentor", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver conflito de horário")
    void deveLancarExcecaoQuandoConflitoComOutraSessao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mentor));
        when(disponibilidadeRepository.existsByMentorIdAndHorario(any(), any(), any())).thenReturn(true);
        when(sessaoRepository.existsConflito(any(), any(), any())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));

        assertEquals("Já existe uma sessão agendada neste horário", ex.getMessage());
    }

    @Test
    @DisplayName("Deve agendar sessão ONLINE com sucesso")
    void deveAgendarOnlineComSucesso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mentor));
        when(disponibilidadeRepository.existsByMentorIdAndHorario(any(), any(), any())).thenReturn(true);
        when(sessaoRepository.existsConflito(any(), any(), any())).thenReturn(false);
        when(requestRepository.findByMentoradoIdAndMentorIdAndStatus(
                anyLong(), anyLong(), any(MentoriaStatus.class)))
                .thenReturn(Collections.emptyList());
        when(requestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(inv -> inv.getArgument(0));

        Sessao resultado = agendamentoService.agendar(mentorado, dto);

        assertNotNull(resultado);
        assertEquals(SessaoStatus.AGENDADA, resultado.getStatus());
    }

    @Test
    @DisplayName("Deve agendar sessão PRESENCIAL com sucesso")
    void deveAgendarPresencialComSucesso() {
        dto.setFormato(FormatoSessao.PRESENCIAL);
        dto.setEndereco("Rua das Flores, 123");
        dto.setLinkReuniao(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mentor));
        when(disponibilidadeRepository.existsByMentorIdAndHorario(any(), any(), any())).thenReturn(true);
        when(sessaoRepository.existsConflito(any(), any(), any())).thenReturn(false);
        when(requestRepository.findByMentoradoIdAndMentorIdAndStatus(
                anyLong(), anyLong(), any(MentoriaStatus.class)))
                .thenReturn(Collections.emptyList());
        when(requestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(inv -> inv.getArgument(0));

        Sessao resultado = agendamentoService.agendar(mentorado, dto);

        assertNotNull(resultado);
        assertEquals("Rua das Flores, 123", resultado.getEndereco());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a data de fim for anterior ao início")
    void deveLancarExcecaoQuandoDataFimAntesInicio() {
        dto.setDataHoraInicio(LocalDateTime.now().plusDays(2).withHour(11));
        dto.setDataHoraFim(LocalDateTime.now().plusDays(2).withHour(10));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));

        assertEquals("A data de fim deve ser posterior ao início", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o mentor não possuir disponibilidade")
    void deveLancarExcecaoQuandoMentorNaoDisponivel() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mentor));
        when(disponibilidadeRepository.existsByMentorIdAndHorario(any(), any(), any())).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.agendar(mentorado, dto));

        assertEquals("O mentor não possui disponibilidade neste horário", ex.getMessage());
    }
}