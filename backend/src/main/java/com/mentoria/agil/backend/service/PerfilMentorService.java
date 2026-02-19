package com.mentoria.agil.backend.service;

import com.mentoria.agil.backend.interfaces.service.PerfilMentorServiceInterface;
import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.repository.PerfilMentorRepository;
import com.mentoria.agil.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilMentorService implements PerfilMentorServiceInterface {

    private final PerfilMentorRepository perfilMentorRepository;
    private final UserRepository userRepository;

    public PerfilMentorService(PerfilMentorRepository perfilMentorRepository, UserRepository userRepository) {
        this.perfilMentorRepository = perfilMentorRepository;
        this.userRepository = userRepository;
    }

    // Notação que implica que o método ou faz tudo ou não faz nada, se for interrompido volta ao estágio inicial sem consequências
    // na snapshot do início do processo
    @Transactional
    public PerfilMentor criarPerfilMentor(User user, String especializacao, String experiencias, String formacao) {
        // Verifica se o usuário já tem perfil de mentor
        if (user.getPerfilMentor() != null) {
            throw new IllegalStateException("Usuário já possui perfil de mentor");
        }

        // Atualiza a role do usuário para MENTOR
        user.setRole(com.mentoria.agil.backend.model.Role.MENTOR);
        
        // Cria o perfil
        PerfilMentor perfil = new PerfilMentor(especializacao, experiencias, user);
        perfil.setFormacao(formacao);
        
        // Estabelece o relacionamento bidirecional
        user.setPerfilMentor(perfil);
        
        // Salva o usuário (cascade deve salvar o perfil como consequência)
        userRepository.save(user);
        
        return perfil;
    }

    public PerfilMentor buscarPorId(Long id) {
        return perfilMentorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Perfil de mentor não encontrado"));
    }

    public List<PerfilMentor> listarTodos() {
        return perfilMentorRepository.findAll();
    }

    @Transactional
    public void deletar(Long id) {
        PerfilMentor perfil = buscarPorId(id);
        User user = perfil.getUser();
        user.setPerfilMentor(null);
        user.setRole(com.mentoria.agil.backend.model.Role.VISITANTE); // Volta para VISITANTE
        userRepository.save(user);
        perfilMentorRepository.delete(perfil);
    }
    
    @Transactional
    public PerfilMentor atualizar(User user, PerfilMentor perfil) {
        // Verifica se o perfil pertence ao usuário
        if (!perfil.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Perfil não pertence ao usuário");
        }
    
        // Salva o usuário (com nome e email atualizados)
        userRepository.save(user);
    
        // Salva o perfil atualizado
        return perfilMentorRepository.save(perfil);
    }
}
