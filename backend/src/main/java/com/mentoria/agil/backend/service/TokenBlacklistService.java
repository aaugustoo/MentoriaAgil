package com.mentoria.agil.backend.service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    private final JwtService jwtService;
    
    public TokenBlacklistService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    
    public void invalidateToken(String token) {
        String cleanToken = token.replace("Bearer ", "").trim();
        Date expiration = jwtService.getExpirationFromToken(cleanToken);
        
        blacklistedTokens.put(cleanToken, expiration.getTime());
        cleanExpiredTokens();
    }
    
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token.trim());
    }

    private void cleanExpiredTokens() {
        long now = System.currentTimeMillis();  
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}