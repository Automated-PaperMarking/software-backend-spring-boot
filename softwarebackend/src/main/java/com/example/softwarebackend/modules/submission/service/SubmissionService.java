package com.example.softwarebackend.modules.submission.service;

import com.example.softwarebackend.modules.submission.dto.GradedSubmissionDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionCreateRequestDTO;
import com.example.softwarebackend.modules.submission.dto.SubmissionResponseDTO;
import jakarta.transaction.Transactional;

public interface SubmissionService {


    @Transactional
    void addSubmission(SubmissionCreateRequestDTO submissionCreateRequestDTO);

    @Transactional
    void updateSubmission(GradedSubmissionDTO submissionDTO);

    @Transactional
    void updateFailedGSubmission(String resultId);

    @Transactional
    SubmissionResponseDTO getSubmissionById(String submissionId);
}
