package com.example.softwarebackend.modules.submission.service;

import com.example.softwarebackend.modules.constest.services.ContestService;
import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardUpdateDTO;
import com.example.softwarebackend.modules.leaderboard.service.LeaderBoardService;
import com.example.softwarebackend.modules.problem.services.ProblemService;
import com.example.softwarebackend.modules.submission.dto.GradedSubmissionDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionCreateRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionPendingRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionResponseDTO;
import com.example.softwarebackend.modules.submission.kafka.SubmissionProducer;
import com.example.softwarebackend.modules.submission.mapper.SubmissionMapper;
import com.example.softwarebackend.modules.submission.repository.SubmissionRepository;
import com.example.softwarebackend.modules.user.services.UserService;
import com.example.softwarebackend.shared.entities.Submission;
import com.example.softwarebackend.shared.enums.GradingResultStatus;
import com.example.softwarebackend.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
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
    private final LeaderBoardService leaderBoardService;

    @Transactional
    @Override
    public String addSubmission(SubmissionCreateRequestDTO submissionCreateRequestDTO) {
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

        var person = contest.getParticipants().stream().findFirst();
        if(person.isPresent()) {
            LOGGER.info(person.get().getEmail());
        }
        //check if student is enrolled in the contest
        if (!contest.getParticipants().contains(student) && !contest.getAuthor().equals(student)) {
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
        Submission submission;

        // Check if there's already one submission per student per problem per contest
        Optional<Submission> uniqueSubmissionPerProblemPerStudentPerContest = contest.getSubmissions()
                .stream()
                .filter(
                        s->{
                            return s.getProblem().getId().equals(problem.getId()) &&
                                    s.getStudent().getId().equals(student.getId());
                        }
                ).findFirst();



        if(uniqueSubmissionPerProblemPerStudentPerContest.isEmpty()){
             submission = submissionRepository.save(
                    Submission.builder()
                            .code(submissionCreateRequestDTO.getCode())
                            .language(submissionCreateRequestDTO.getLanguage())
                            .submissionType(submissionCreateRequestDTO.getSubmissionType())
                            .student(student)
                            .problem(problem)
                            .contest(contest)
                            .understandingLogic(0)
                            .correctnessScore(0)
                            .readabilityScore(0)
                            .totalScore(0)
                            .gradingResultStatus(GradingResultStatus.PENDING)
                            .build()
            );

        }else{
            // else update existing submission
            submission = uniqueSubmissionPerProblemPerStudentPerContest.get();
            submission.setCode(submissionCreateRequestDTO.getCode());
            submission.setLanguage(submissionCreateRequestDTO.getLanguage());
            submission.setSubmissionType(submissionCreateRequestDTO.getSubmissionType());
            submission.setGradingResultStatus(GradingResultStatus.PENDING);

            submission = submissionRepository.save(submission);
        }



        //publish for grading
        SubmissionPendingRequestDTO submissionPendingRequestDTO = SubmissionMapper.getSubmissionPendingRequestDTO(submissionCreateRequestDTO, submission);
        submissionProducer.publish(submissionPendingRequestDTO);

        LOGGER.info("submission added with id: {}", submission.getId());
        return submission.getId().toString();

    }



    @Transactional
    @Override
    public void updateSubmission(GradedSubmissionDTO submissionDTO) {
        var existingSubmission = submissionRepository.findById(UUID.fromString(submissionDTO.getSubmissionId()));
        if (existingSubmission.isPresent()) {
            var submissionToUpdate = SubmissionMapper.getSubmission(submissionDTO, existingSubmission);
            submissionRepository.save(submissionToUpdate);
            LOGGER.info("Updated submission with id: {}", submissionDTO.getSubmissionId());

            //update leaderboard
            leaderBoardService.updateLeaderBoard(
                    new LeaderBoardUpdateDTO(
                            submissionToUpdate.getStudent().getId(),
                            UUID.fromString(submissionDTO.getContestId()),
                            UUID.fromString(submissionDTO.getProblemId()),
                            submissionDTO.getTotalScore()
                    )
            );

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
                .comment(submission.getComment())
                .gradingResultStatus(submission.getGradingResultStatus().name())
                .createdAt(submission.getCreatedAt().toString())
                .updatedAt(submission.getUpdatedAt().toString())
                .build();
    }

}
