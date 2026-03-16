package com.mentoria.agil.backend.repository;

import com.mentoria.agil.backend.model.SessaoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessaoMaterialRepository extends JpaRepository<SessaoMaterial, Long> {
    List<SessaoMaterial> findBySessaoId(Long sessaoId);
    boolean existsBySessaoIdAndMaterialId(Long sessaoId, Long materialId);
}
