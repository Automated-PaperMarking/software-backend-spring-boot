package com.example.softwarebackend.modules.submission.service;

import com.example.softwarebackend.modules.constest.services.ContestService;
import com.example.softwarebackend.modules.problem.services.ProblemService;
import com.example.softwarebackend.modules.submission.dto.GradedSubmissionDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionCreateRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionPendingRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionResponseDTO;
import com.example.softwarebackend.modules.submission.kafka.SubmissionProducer;
import com.example.softwarebackend.modules.submission.repository.SubmissionRepository;
import com.example.softwarebackend.modules.user.services.UserService;
import com.example.softwarebackend.shared.enums.GradingResultStatus;
import com.example.softwarebackend.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    //logger
    private final static Logger LOGGER = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionRepository submissionRepository;
    private final UserService userService;
    private final ProblemService problemService;
    private final ContestService contestService;
    private final SubmissionProducer submissionProducer;

    @Transactional
    @Override
    public void addSubmission(SubmissionCreateRequestDTO submissionCreateRequestDTO) {
        var student = userService.getUserEntityById(UUID.fromString(submissionCreateRequestDTO.getStudentId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found  ")
                );
        var problem = problemService.getProblemEntityById(UUID.fromString(submissionCreateRequestDTO.getProblemId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Problem not found  ")
                );
        var contest = contestService.getContestEntityById(UUID.fromString(submissionCreateRequestDTO.getContestId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contest not found  ")
                );
        //check if student is enrolled in the contest
        if (!contest.getParticipants().contains(student) || !contest.getAuthor().equals(student)) {
            throw new IllegalArgumentException("Student is not enrolled in the contest.");
        }

        // Check if current time is within contest start and end time
        OffsetDateTime currentTime = OffsetDateTime.now();
        switch (currentTime){
            case OffsetDateTime time when time.isBefore(contest.getStartTime()) ->
                    throw new IllegalArgumentException("Contest has not started yet.");
            case OffsetDateTime time when time.isAfter(contest.getEndTime()) ->
                    throw new IllegalArgumentException("Contest has already ended.");
            default -> {
                // within contest time
            }
        }
        // Check if problem belongs to the contest
        if (!contest.getProblems().contains(problem)) {
            throw new IllegalArgumentException("Problem does not belong to the specified contest.");
        }

        var submission = submissionRepository.save(
                com.example.softwarebackend.shared.entities.Submission.builder()
                        .code(submissionCreateRequestDTO.getCode())
                        .language(submissionCreateRequestDTO.getLanguage())
                        .submissionType(submissionCreateRequestDTO.getSubmissionType())
                        .student(student)
                        .problem(problem)
                        .understandingLogic(0)
                        .correctnessScore(0)
                        .readabilityScore(0)
                        .totalScore(0)
                        .gradingResultStatus(com.example.softwarebackend.shared.enums.GradingResultStatus.PENDING)
                        .build()
        );

        //publish for grading
        SubmissionPendingRequestDTO submissionPendingRequestDTO = new SubmissionPendingRequestDTO();
        submissionPendingRequestDTO.setSubmissionId(submission.getId().toString());
        submissionPendingRequestDTO.setCode(submission.getCode());
        submissionPendingRequestDTO.setLanguage(submission.getLanguage());
        submissionPendingRequestDTO.setProblem(submission.getProblem().getStatement());
        submissionPendingRequestDTO.setSubmissionType(submission.getSubmissionType());
        submissionProducer.publish(submissionPendingRequestDTO);

        LOGGER.info("New submission added with id: {}", submission.getId());

    }

    @Transactional
    @Override
    public void updateSubmission(GradedSubmissionDTO submissionDTO) {
        var existingSubmission = submissionRepository.findById(UUID.fromString(submissionDTO.getSubmissionId()));
        if (existingSubmission.isPresent()) {
            var submissionToUpdate = existingSubmission.get();
            submissionToUpdate.setUnderstandingLogic(submissionDTO.getUnderstandingLogic());
            submissionToUpdate.setCorrectnessScore(submissionDTO.getCorrectnessScore());
            submissionToUpdate.setReadabilityScore(submissionDTO.getReadabilityScore());
            submissionToUpdate.setTotalScore(submissionDTO.getTotalScore());
            submissionToUpdate.setGradingResultStatus(com.example.softwarebackend.shared.enums.GradingResultStatus.COMPLETED);
            submissionRepository.save(submissionToUpdate);
            LOGGER.info("Updated submission with id: {}", submissionDTO.getSubmissionId());
        } else {
            LOGGER.warn("Submission not found with id: {}", submissionDTO.getSubmissionId());
        }

    }

    @Transactional
    @Override
    public void updateFailedGSubmission(String resultId){
        var existingResult = submissionRepository.findById(UUID.fromString(resultId));
        if(existingResult.isPresent()){
            existingResult.get().setGradingResultStatus(GradingResultStatus.FAILED);
            submissionRepository.save(existingResult.get());
            LOGGER.info("Updated Failed graded result for student ID: {}", resultId);

        }
    }

    @Transactional
    @Override
    public SubmissionResponseDTO getSubmissionById(String submissionId) {
        var submission = submissionRepository.findById(UUID.fromString(submissionId))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Submission not found with id: " + submissionId)
                );
        return SubmissionResponseDTO.builder()
                .id(submission.getId().toString())
                .code(submission.getCode())
                .submissionType(submission.getSubmissionType())
                .language(submission.getLanguage())
                .studentId(submission.getStudent().getId().toString())
                .problemId(submission.getProblem().getId().toString())
                .understandingLogic(submission.getUnderstandingLogic())
                .correctnessScore(submission.getCorrectnessScore())
                .readabilityScore(submission.getReadabilityScore())
                .totalScore(submission.getTotalScore())
                .gradingResultStatus(submission.getGradingResultStatus().name())
                .createdAt(submission.getCreatedAt().toString())
                .updatedAt(submission.getUpdatedAt().toString())
                .build();
    }




}
