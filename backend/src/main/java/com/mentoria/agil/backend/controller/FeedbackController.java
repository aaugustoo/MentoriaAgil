package com.mentoria.agil.backend.controller;

import com.mentoria.agil.backend.dto.FeedbackRequestDTO;
import com.mentoria.agil.backend.dto.response.FeedbackResponseDTO;
import com.mentoria.agil.backend.interfaces.service.FeedbackServiceInterface;
import com.mentoria.agil.backend.model.Feedback;
import com.mentoria.agil.backend.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessoes")
public class FeedbackController {

    private final FeedbackServiceInterface feedbackService;

    public FeedbackController(FeedbackServiceInterface feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<FeedbackResponseDTO> criarFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO dto) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User mentorado = (User) userDetails;

        Feedback feedback = feedbackService.criarFeedback(id, mentorado, dto);
        FeedbackResponseDTO response = new FeedbackResponseDTO(feedback);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}