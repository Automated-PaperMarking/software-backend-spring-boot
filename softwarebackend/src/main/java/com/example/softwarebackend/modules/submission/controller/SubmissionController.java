package com.example.softwarebackend.modules.submission.controller;

import com.example.softwarebackend.modules.submission.dto.SubmissionCreateRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionResponseDTO;
import com.example.softwarebackend.modules.submission.kafka.SubmissionProducer;
import com.example.softwarebackend.modules.submission.service.SubmissionService;
import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes endpoints to submit code and query results.
 * - POST /api/submissions -> enqueue submission to Kafka (returns ACK)
 * - GET /api/submissions/{studentId} -> get current status/result
 */
@RestController
@RequestMapping("/api/submissions")
@AllArgsConstructor
public class SubmissionController {

    private final SubmissionProducer producer;
    private final SubmissionService submissionService;


    @PostMapping
    public ResponseEntity<ApiResponseDTO<?>> submit(@RequestBody SubmissionCreateRequestDTO submission) {
        submissionService.addSubmission(submission);
        ApiResponseDTO<?> apiResponseDTO = new ApiResponseDTO<>("203", "Submission received and is being processed.", null,true);
        return ResponseEntity.ok(apiResponseDTO);

    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<ApiResponseDTO<SubmissionResponseDTO>> getSubmissionStatus(@PathVariable String submissionId) {
        SubmissionResponseDTO submissionResponseDTO = submissionService.getSubmissionById(submissionId);
        ApiResponseDTO<SubmissionResponseDTO> apiResponseDTO = new ApiResponseDTO<SubmissionResponseDTO>("200", "Submission retrieved successfully.", submissionResponseDTO,true);
        return ResponseEntity.ok(apiResponseDTO);   }




}
