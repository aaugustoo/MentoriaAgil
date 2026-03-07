package com.mentoria.agil.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentorship_requests")
public class MentoriaRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentorado_id", nullable = false)
    private User mentorado;  

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;  

    @Column(nullable = false, length = 500)
    private String message;  

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentoriaStatus status;  // PENDING, ACCEPTED, REJECTED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public User getMentorado(){
        return this.mentorado;
    }

    public void setMentorado(User mentorado){
        this.mentorado = mentorado;
    }

    public User getMentor(){
        return this.mentor;
    }

    public void setMentor(User mentor){
        this.mentor = mentor;
    }

        public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MentoriaStatus getStatus() {
        return status;
    }

    public void setStatus(MentoriaStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}