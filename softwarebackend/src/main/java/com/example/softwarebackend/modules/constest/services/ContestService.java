package com.example.softwarebackend.modules.constest.services;


import com.example.softwarebackend.modules.constest.dto.AddProblemsToContestDTO;
import com.example.softwarebackend.modules.constest.dto.ContestCreateDTO;
import com.example.softwarebackend.modules.constest.dto.ContestResponseDTO;
import com.example.softwarebackend.modules.constest.dto.RemoveProblemsToContestDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface ContestService {

    public PageResponseDTO<ContestResponseDTO> getAllContests(String search, int page, int size, String[] sort);

    public ContestResponseDTO findById(UUID id);

    @Transactional
    public void deleteById(UUID id);

    public void createContest(ContestCreateDTO contestCreateDTO);

    @Transactional
    public void assignProblemsToContest(AddProblemsToContestDTO addProblemsToContestDTO);

    @Transactional
    void removeProblemFromContest(RemoveProblemsToContestDTO removeProblemsToContestDTO);
}
