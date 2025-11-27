package com.example.softwarebackend.modules.problem.services;

import com.example.softwarebackend.modules.problem.dto.ProblemCreateDTO;
import com.example.softwarebackend.modules.problem.dto.ProblemResponseDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import com.example.softwarebackend.shared.entities.Problem;

import java.util.Optional;
import java.util.UUID;

public interface ProblemService {

    PageResponseDTO<ProblemResponseDTO> getAllProblems(String search, int page, int size, String[] sort);

    ProblemResponseDTO findById(UUID id);

    void deleteById(UUID id);

    void createProblem(ProblemCreateDTO problemCreateDTO);

    public Optional<Problem> getProblemEntityById(UUID id);
}
