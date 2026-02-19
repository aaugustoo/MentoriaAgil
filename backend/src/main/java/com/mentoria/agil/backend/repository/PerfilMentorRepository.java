package com.mentoria.agil.backend.repository;

import com.mentoria.agil.backend.model.PerfilMentor;
import com.mentoria.agil.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilMentorRepository extends JpaRepository<PerfilMentor, Long> {
    Optional<PerfilMentor> findByUser(User user);
    Optional<PerfilMentor> findByUserEmail(String email);
    boolean existsByUser(User user);
}