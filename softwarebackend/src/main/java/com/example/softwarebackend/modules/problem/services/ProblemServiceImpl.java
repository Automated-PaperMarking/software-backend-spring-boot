package com.example.softwarebackend.modules.problem.services;

import com.example.softwarebackend.modules.problem.dto.ProblemCreateDTO;
import com.example.softwarebackend.modules.problem.dto.ProblemResponseDTO;
import com.example.softwarebackend.modules.problem.mapper.ProblemMapper;
import com.example.softwarebackend.modules.problem.repository.ProblemRepository;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import com.example.softwarebackend.shared.entities.Problem;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private static final Logger logger = LoggerFactory.getLogger(ProblemServiceImpl.class);
    private final ProblemRepository problemRepository;

    @Override
    public PageResponseDTO<ProblemResponseDTO> getAllProblems(String search, int page, int size, String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Problem> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<ProblemResponseDTO> problems;

        if (!Objects.equals(search, "") && search != null) {
            currentPage = problemRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = problemRepository.findAll(pageable);
        }

        problems = (currentPage.getContent()).stream().map(ProblemMapper::toDTO).toList();

        logger.info("Retrieved {} problems", problems.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), problems);
    }

    @Override
    public ProblemResponseDTO findById(UUID id) {
        var problem = problemRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Problem not found with id: " + id)
                );
        return ProblemMapper.toDTO(problem);
    }

    @Override
    public void deleteById(UUID id) {
        var problem = problemRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Problem not found with id: " + id)
                );
        problemRepository.delete(problem);
    }

    @Override
    public void createProblem(ProblemCreateDTO problemCreateDTO) {
        var problem = ProblemMapper.toEntity(problemCreateDTO);
        var savedProblem = problemRepository.save(problem);
        logger.info("Created problem with id: {}", savedProblem.getId());
    }
}
