package com.example.softwarebackend.modules.grader.controller;

import com.example.softwarebackend.modules.grader.dto.CodeSubmission;
import com.example.softwarebackend.modules.grader.dto.ResultResponseDTO;
import com.example.softwarebackend.modules.grader.kafka.SubmissionProducer;
import com.example.softwarebackend.modules.grader.model.GradedResult;
import com.example.softwarebackend.modules.grader.service.GradedResultService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SubmissionController {

    private final SubmissionProducer producer;
    private  final GradedResultService gradedResultService;


    @PostMapping
    public ResponseEntity<?> submit(@RequestBody CodeSubmission submission) {
        // Basic validation; in production add schema validation, size limits, auth
        if (submission.getStudentId() == null || submission.getStudentId().isBlank()) {
            return ResponseEntity.badRequest().body("studentId is required");
        }

        //create result entry with PENDING status
        GradedResult result = gradedResultService.addGradedResult(submission.getStudentId());
        submission.setGradedResultId(result.getId().toString());
        producer.publish(submission);

        // return immediate ack
        return ResponseEntity.accepted().body(Map.of(
                "status", "accepted",
                "resultId", submission.getGradedResultId()
        ));
    }

    @GetMapping("/result")
    public ResponseEntity<?>  getResult(@RequestParam(name = "resultId") String resultId) {
        ResultResponseDTO result= gradedResultService.getResultById(resultId);
        return ResponseEntity.ok(result);

    }


}
