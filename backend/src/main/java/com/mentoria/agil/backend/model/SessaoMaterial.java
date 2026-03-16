package com.mentoria.agil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Associação entre sessão e materiais de apoio
@Entity
@Table(name = "sessao_material")
public class SessaoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "vinculado_em", nullable = false)
    private LocalDateTime vinculadoEm;

    @PrePersist
    protected void onCreate() {
        vinculadoEm = LocalDateTime.now();
    }

    public SessaoMaterial() {}

    public SessaoMaterial(Sessao sessao, Material material) {
        this.sessao = sessao;
        this.material = material;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public LocalDateTime getVinculadoEm() {
        return vinculadoEm;
    }

    public void setVinculadoEm(LocalDateTime vinculadoEm) {
        this.vinculadoEm = vinculadoEm;
    }
}
