package com.example.softwarebackend.modules.grader.service;


import com.example.softwarebackend.modules.grader.dto.GradedResultDTO;
import com.example.softwarebackend.modules.grader.dto.ResultResponseDTO;
import com.example.softwarebackend.modules.grader.model.GradedResult;
import com.example.softwarebackend.modules.grader.model.GradingResultStatus;
import com.example.softwarebackend.modules.grader.repository.GradedResultRepository;
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
        Optional<GradedResult> existingResult = gradedResultRepository.findById(UUID.fromString(gradedResult.getGradedResultId()));
        if(existingResult.isPresent()){
            GradedResult resultToUpdate = existingResult.get();
            resultToUpdate.setUnderstandingLogic(gradedResult.getUnderstandingLogic());
            resultToUpdate.setCorrectnessScore(gradedResult.getCorrectnessScore());
            resultToUpdate.setReadabilityScore(gradedResult.getReadabilityScore());
            resultToUpdate.setTotalScore(gradedResult.getTotalScore());
            resultToUpdate.setGradingResultStatus(GradingResultStatus.COMPLETED);
            gradedResultRepository.save(resultToUpdate);
        } else {
            logger.warn("Graded result not found for student ID: {}", gradedResult.getStudentId());
        }
        logger.info("Updated graded result for student ID: {}", gradedResult.getStudentId());
    }

    public  void updateFailedGradingResult(String resultId){
        Optional<GradedResult> existingResult = gradedResultRepository.findById(UUID.fromString(resultId));
        if(existingResult.isPresent()){
            existingResult.get().setGradingResultStatus(GradingResultStatus.FAILED);
            gradedResultRepository.save(existingResult.get());
            logger.info("Updated Failed graded result for student ID: {}", resultId);

        }
    }
    public ResultResponseDTO getResultById(String resultId){
        Optional<GradedResult> existingResult = gradedResultRepository.findById(UUID.fromString(resultId));
        if(existingResult.isPresent()){
            GradedResult result = existingResult.get();
            return new ResultResponseDTO(
                    result.getId().toString(),
                    result.getStudentId(),
                    result.getUnderstandingLogic(),
                    result.getCorrectnessScore(),
                    result.getReadabilityScore(),
                    result.getTotalScore(),
                    result.getGradingResultStatus().name()
            );
        } else {
            logger.warn("Result not found for student ID: {}", resultId);
            return null;
        }

    }



}
