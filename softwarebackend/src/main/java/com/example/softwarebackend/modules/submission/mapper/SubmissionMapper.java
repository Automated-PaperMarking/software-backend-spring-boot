package com.example.softwarebackend.modules.submission.mapper;

import com.example.softwarebackend.modules.submission.dto.GradedSubmissionDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionCreateRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionPendingRequestDTO;
import com.example.softwarebackend.shared.entities.Submission;
import com.example.softwarebackend.shared.enums.GradingResultStatus;

import java.util.Optional;

public class SubmissionMapper {
    public static SubmissionPendingRequestDTO getSubmissionPendingRequestDTO(SubmissionCreateRequestDTO submissionCreateRequestDTO, Submission submission) {
        SubmissionPendingRequestDTO submissionPendingRequestDTO = new SubmissionPendingRequestDTO();
        submissionPendingRequestDTO.setSubmissionId(submission.getId().toString());
        submissionPendingRequestDTO.setCode(submission.getCode());
        submissionPendingRequestDTO.setLanguage(submission.getLanguage());
        submissionPendingRequestDTO.setProblem(submission.getProblem().getStatement());
        submissionPendingRequestDTO.setSubmissionType(submission.getSubmissionType());
        submissionPendingRequestDTO.setContestId(submissionCreateRequestDTO.getContestId());
        submissionPendingRequestDTO.setProblemId(submissionCreateRequestDTO.getProblemId());

        return submissionPendingRequestDTO;
    }
    public static Submission getSubmission(GradedSubmissionDTO submissionDTO, Optional<Submission> existingSubmission) {
        var submissionToUpdate = existingSubmission.get();
        submissionToUpdate.setUnderstandingLogic(submissionDTO.getUnderstandingLogic());
        submissionToUpdate.setCorrectnessScore(submissionDTO.getCorrectnessScore());
        submissionToUpdate.setReadabilityScore(submissionDTO.getReadabilityScore());
        submissionToUpdate.setTotalScore(submissionDTO.getTotalScore());
        submissionToUpdate.setGradingResultStatus(GradingResultStatus.COMPLETED);
        submissionToUpdate.setComment(submissionDTO.getComment());
        return submissionToUpdate;
    }
}
