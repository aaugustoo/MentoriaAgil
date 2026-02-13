package com.mentoria.agil.backend.service;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import com.mentoria.agil.backend.service.TokenService;

@Service
public class TokenBlacklistService {
    
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    
    private final TokenService tokenService;
    
    public TokenBlacklistService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    
    public void invalidateToken(String token) {
        String cleanToken = token.replace("Bearer ", "");
        Date expiration = tokenService.getExpirationFromTokenForBlacklist(cleanToken);
        
        blacklistedTokens.put(cleanToken, expiration.getTime());
        cleanExpiredTokens();
    }
    
    public boolean isTokenBlacklisted(String token) {
        //Limpa tokens expirados antes de verificar
        cleanExpiredTokens();
        return blacklistedTokens.containsKey(token);
    }
    
     private void cleanExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
    }
    
    public int getBlacklistSize() {
        cleanExpiredTokens();
        return blacklistedTokens.size();
    }
}