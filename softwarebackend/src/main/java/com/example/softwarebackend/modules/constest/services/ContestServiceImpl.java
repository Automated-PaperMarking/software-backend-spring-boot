package com.example.softwarebackend.modules.constest.services;

import com.example.softwarebackend.modules.constest.dto.ContestCreateDTO;
import com.example.softwarebackend.modules.constest.dto.ContestResponseDTO;
import com.example.softwarebackend.modules.constest.mapper.ContestMapper;
import com.example.softwarebackend.modules.constest.repository.ContestRepository;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import com.example.softwarebackend.shared.entities.Contest;
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
public class ContestServiceImpl  implements ContestService {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(ContestServiceImpl.class);
    private final ContestRepository contestRepository;

    @Override
    public PageResponseDTO<ContestResponseDTO> getAllProjects(String search, int page, int size, String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Contest> currentPage;
        int currentPageNumber=pageable.getPageNumber();
        List<ContestResponseDTO> projects ;
        if (!Objects.equals(search, "") && search != null) {
            currentPage =contestRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = contestRepository.findAll(pageable);
        }
        projects= (currentPage.getContent()).stream().map(ContestMapper::toDTO).toList();

        logger.info("Retrieved {} projects", projects.size());

        return new PageResponseDTO<ContestResponseDTO>(currentPageNumber,currentPage.getTotalPages(),projects);

    }

    @Override
    public ContestResponseDTO findById(UUID id) {
        var contest = contestRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id: " + id)
                );
        return ContestMapper.toDTO(contest);
    }

    @Override
    public void deleteById(UUID id) {
        var contest = contestRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id: " + id)
                );
        contestRepository.delete(contest);

    }

    @Override
    public void createContest(ContestCreateDTO contestCreateDTO) {
        var contest = ContestMapper.toEntity(contestCreateDTO);
        var savedContest = contestRepository.save(contest);
    }
}
