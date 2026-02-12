package com.mentoria.agil.backend.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    
    private final ConcurrentHashMap<String, Boolean> blacklistedTokens = new ConcurrentHashMap<>();
    
    public void invalidateToken(String token) {
        blacklistedTokens.put(token, true);
    }
    
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }
}