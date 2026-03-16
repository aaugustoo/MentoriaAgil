package com.mentoria.agil.backend.interfaces.service;

import com.mentoria.agil.backend.dto.MaterialRequestDTO;
import com.mentoria.agil.backend.model.Material;
import com.mentoria.agil.backend.model.User;
import java.util.List;

public interface MaterialServiceInterface {
    public Material criarMaterial(User mentor, MaterialRequestDTO dto);
    public List<Material> listarMateriaisPorMentorado(User mentorado);
}
