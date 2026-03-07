package com.mentoria.agil.backend.repository;

import com.mentoria.agil.backend.model.MentoriaRequest;
import com.mentoria.agil.backend.model.MentoriaStatus;
import com.mentoria.agil.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoriaRequestRepository extends JpaRepository<MentoriaRequest, Long> {
    boolean existsByMentoradoAndMentorAndStatus(User mentee, User mentor, MentoriaStatus status);
}