package com.mentoria.agil.backend.interfaces.facade;

import com.mentoria.agil.backend.dto.LoginDTO;
import com.mentoria.agil.backend.dto.UserRequestDTO;
import com.mentoria.agil.backend.dto.response.LoginResponseDTO;

public interface AuthFacadeInterface {
    void registrarNovoUsuario(UserRequestDTO dto);
    LoginResponseDTO autenticar(LoginDTO dto);
    void encerrarSessao(String authHeader);
}