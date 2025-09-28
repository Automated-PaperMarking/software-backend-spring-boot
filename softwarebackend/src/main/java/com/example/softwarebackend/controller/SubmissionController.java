package com.example.softwarebackend.controller;

import com.example.softwarebackend.dto.CodeSubmission;
import com.example.softwarebackend.kafka.SubmissionProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Exposes endpoints to submit code and query results.
 * - POST /api/submissions -> enqueue submission to Kafka (returns ACK)
 * - GET /api/submissions/{studentId} -> get current status/result
 */
@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionProducer producer;

    public SubmissionController(SubmissionProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<?> submit(@RequestBody CodeSubmission submission) {
        // Basic validation; in production add schema validation, size limits, auth
        if (submission.getStudentId() == null || submission.getStudentId().isBlank()) {
            return ResponseEntity.badRequest().body("studentId is required");
        }

        producer.publish(submission);

        // return immediate ack
        return ResponseEntity.accepted().body(Map.of(
                "status", "accepted",
                "studentId", submission.getStudentId()
        ));
    }


}
