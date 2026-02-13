package com.mentoria.agil.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mentoria.agil.backend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;

    private final TokenBlacklistService tokenBlacklistService;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public boolean isValidToken(String token) {
        try {
            //Verifica se está na blacklist (pós-logout)
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return false; // Token invalidado pelo logout
            }
            
            //Verifica assinatura, issuer e expiração
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token);
            
            return true;
            
        } catch (JWTVerificationException e) {
            return false; // Token inválido (assinatura, expirado, issuer errado)
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public Date getExpirationFromTokenForBlacklist(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token.replace("Bearer ", ""));
            Date expiration = decodedJWT.getExpiresAt();
            return expiration != null ? expiration : 
                   new Date(System.currentTimeMillis() + 86400000); //24h fallback
        } catch (JWTDecodeException e) {
            return new Date(System.currentTimeMillis() + 3600000); // 1h fallback
        }
    }
}