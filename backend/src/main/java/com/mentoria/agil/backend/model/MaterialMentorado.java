package com.mentoria.agil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidade de associação entre material de apoio e mentorado 
@Entity
@Table(name = "material_mentorado")
public class MaterialMentorado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "mentorado_id", nullable = false)
    private User mentorado;  // deve ter role ESTUDANTE

    @Column(nullable = false)
    private LocalDateTime compartilhadoEm;

    @PrePersist
    protected void onCreate() {
        compartilhadoEm = LocalDateTime.now();
    }

    public MaterialMentorado() {}

    public MaterialMentorado(Material material, User mentorado) {
        this.material = material;
        this.mentorado = mentorado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public User getMentorado() {
        return mentorado;
    }

    public void setMentorado(User mentorado) {
        this.mentorado = mentorado;
    }

    public LocalDateTime getCompartilhadoEm() {
        return compartilhadoEm;
    }

    public void setCompartilhadoEm(LocalDateTime compartilhadoEm) {
        this.compartilhadoEm = compartilhadoEm;
    }
}
