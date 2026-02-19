package com.mentoria.agil.backend.interfaces.service;

import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;

import java.util.List;

public interface PerfilMentorServiceInterface {
    public PerfilMentor criarPerfilMentor(User user, String especializacao, String experiencias, String formacao);
    public PerfilMentor buscarPorId(Long id);
    public List<PerfilMentor> listarTodos();
    public void deletar(Long id);
    public PerfilMentor atualizar(User user, PerfilMentor perfil);
}
