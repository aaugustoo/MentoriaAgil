package com.mentoria.agil.backend.repository;

import com.mentoria.agil.backend.model.Material;
import com.mentoria.agil.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByMentor(User mentor);
}
