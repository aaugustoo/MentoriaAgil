package com.mentoria.agil.backend.dto.response;

import com.mentoria.agil.backend.enums.MentoriaStatus;
import com.mentoria.agil.backend.model.MentoriaRequest;

import java.time.LocalDateTime;

public class MentoriaResponseDTO {
    private Long id;
    private Long mentoradoId;
    private String mentoradoName;
    private Long mentorId;
    private String mentorName;
    private String message;
    private MentoriaStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MentoriaResponseDTO() {
    }

    public MentoriaResponseDTO(MentoriaRequest request) {
        this.id = request.getId();
        this.mentoradoId = request.getMentorado().getId();
        this.mentoradoName = request.getMentorado().getName();
        this.mentorId = request.getMentor().getId();
        this.mentorName = request.getMentor().getName();
        this.message = request.getMessage();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.updatedAt = request.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMentoradoId() {
        return mentoradoId;
    }

    public void setMentoradoId(Long mentoradoId) {
        this.mentoradoId = mentoradoId;
    }

    public String getMentoradoName() {
        return mentoradoName;
    }

    public void setMentoradoName(String mentoradoName) {
        this.mentoradoName = mentoradoName;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
