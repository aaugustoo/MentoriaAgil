package com.mentoria.agil.backend.repository;

import com.mentoria.agil.backend.model.Feedback;
import com.mentoria.agil.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsBySessaoId(Long sessaoId);

    @Query("SELECT AVG(f.nota) FROM Feedback f WHERE f.sessao.mentor = :mentor")
    Double calcularMediaNotasPorMentor(@Param("mentor") User mentor);
}
