package com.example.softwarebackend.service;


import com.example.softwarebackend.dto.GradedResultDTO;
import com.example.softwarebackend.model.GradedResult;
import com.example.softwarebackend.model.GradingResultStatus;
import com.example.softwarebackend.repository.GradedResultRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GradedResultService {
    private static final Logger logger = LoggerFactory.getLogger(GradedResultService.class);
    private final GradedResultRepository gradedResultRepository;

    public GradedResult addGradedResult(String studentId){
        GradedResult newGradedResult = new GradedResult();
        newGradedResult.setStudentId(studentId);
        newGradedResult.setCorrectnessScore(0.0);
        newGradedResult.setUnderstandingLogic(0.0);
        newGradedResult.setReadabilityScore(0.0);
        newGradedResult.setTotalScore(0.0);
        newGradedResult.setGradingResultStatus(GradingResultStatus.PENDING);
        logger.info("Added new graded result for student ID: {}", studentId);
        return  gradedResultRepository.save(newGradedResult);


    }

    public void updateGradedResult(GradedResultDTO gradedResult){
        // Validate gradedResultId is not null or empty
        if (gradedResult.getGradedResultId() == null || gradedResult.getGradedResultId().trim().isEmpty()) {
            logger.error("Cannot update graded result: gradedResultId is null or empty for student: {}",
                        gradedResult.getStudentId());
            throw new IllegalArgumentException("GradedResultId cannot be null or empty");
        }

        try {
            Optional<GradedResult> existingResult = gradedResultRepository.findById(UUID.fromString(gradedResult.getGradedResultId()));
            if(existingResult.isPresent()){
                GradedResult resultToUpdate = existingResult.get();
                resultToUpdate.setUnderstandingLogic(gradedResult.getUnderstandingLogic());
                resultToUpdate.setCorrectnessScore(gradedResult.getCorrectnessScore());
                resultToUpdate.setReadabilityScore(gradedResult.getReadabilityScore());
                resultToUpdate.setTotalScore(gradedResult.getTotalScore());
                resultToUpdate.setGradingResultStatus(GradingResultStatus.COMPLETED);
                gradedResultRepository.save(resultToUpdate);
                logger.info("Updated graded result for student ID: {}", gradedResult.getStudentId());
            } else {
                logger.warn("Graded result not found for ID: {} and student ID: {}",
                           gradedResult.getGradedResultId(), gradedResult.getStudentId());
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid UUID format for gradedResultId: {} for student: {}",
                        gradedResult.getGradedResultId(), gradedResult.getStudentId(), e);
            throw new IllegalArgumentException("Invalid gradedResultId format: " + gradedResult.getGradedResultId(), e);
        }
    }

}
