package com.mentoria.agil.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.mentoria.agil.backend.dto.UserDTO;
import com.mentoria.agil.backend.model.User;
import com.mentoria.agil.backend.service.UserService;
import com.mentoria.agil.backend.service.TokenBlacklistService;

//classe de controle de autenticação e autorização
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final TokenBlacklistService tokenBlacklistService;
    private final UserService userService;

    public AuthController(UserService userService, TokenBlacklistService tokenBlacklistService) {
      this.userService = userService;
      this.tokenBlacklistService = tokenBlacklistService;
  }
  
    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@RequestBody UserDTO userDTO) {
        try {
            User novoUsuario = userService.salvarUsuario(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso. ID: " + novoUsuario.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
  
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.invalidateToken(token);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}