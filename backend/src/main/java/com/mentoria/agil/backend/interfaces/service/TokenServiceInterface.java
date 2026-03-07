package com.mentoria.agil.backend.interfaces.service;

import java.util.Date;

import com.mentoria.agil.backend.model.User;

public interface TokenServiceInterface {
    String generateToken(User user);
    String getSubjectFromToken(String token);
    Date getExpirationFromToken(String token);
}