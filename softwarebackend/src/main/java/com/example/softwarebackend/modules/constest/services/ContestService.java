package com.example.softwarebackend.modules.constest.services;


import com.example.softwarebackend.modules.constest.dto.*;
import com.example.softwarebackend.modules.problem.dto.ProblemResponseDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface ContestService {

    public PageResponseDTO<ContestResponseDTO> getAllContests(String search, int page, int size, String[] sort);

    public ContestResponseDTO findById(UUID id);

    List<ProblemResponseDTO> getContestProblems(UUID contestId);

    //enroll to contest
    @Transactional
    void enrollToContest(ContestEnrollRequestDTO contestEnrollRequestDTO);

    @Transactional
    public void deleteById(UUID id);

    @Transactional
    public void createContest(ContestCreateDTO contestCreateDTO);

    @Transactional
    public void assignProblemsToContest(AddProblemsToContestDTO addProblemsToContestDTO);

    @Transactional
    void removeProblemFromContest(RemoveProblemsToContestDTO removeProblemsToContestDTO);
}
