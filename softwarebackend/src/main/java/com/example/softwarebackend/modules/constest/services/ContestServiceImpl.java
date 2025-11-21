package com.example.softwarebackend.modules.constest.services;

import com.example.softwarebackend.modules.constest.dto.AddProblemsToContestDTO;
import com.example.softwarebackend.modules.constest.dto.ContestCreateDTO;
import com.example.softwarebackend.modules.constest.dto.ContestResponseDTO;
import com.example.softwarebackend.modules.constest.mapper.ContestMapper;
import com.example.softwarebackend.modules.constest.repository.ContestRepository;
import com.example.softwarebackend.modules.auth.services.JwtService;
import com.example.softwarebackend.modules.problem.services.ProblemService;
import com.example.softwarebackend.modules.user.services.UserService;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import com.example.softwarebackend.shared.entities.Contest;
import com.example.softwarebackend.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final ProblemService problemService;

    @Transactional
    @Override
    public PageResponseDTO<ContestResponseDTO> getAllContests(String search, int page, int size, String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Contest> currentPage;
        int currentPageNumber=pageable.getPageNumber();
        List<ContestResponseDTO> contests;
        if (!Objects.equals(search, "") && search != null) {
            currentPage =contestRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = contestRepository.findAll(pageable);
        }
        contests = (currentPage.getContent()).stream().map(ContestMapper::toDTO).toList();

        logger.info("Retrieved {} contests", contests.size());

        return new PageResponseDTO<ContestResponseDTO>(currentPageNumber,currentPage.getTotalPages(), contests);

    }

    @Override
    public ContestResponseDTO findById(UUID id) {
        var contest = contestRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id: " + id)
                );
        return ContestMapper.toDTO(contest);
    }

    @Transactional
    @Override
    public void createContest(ContestCreateDTO contestCreateDTO) {
        var contest = ContestMapper.toEntity(contestCreateDTO);
        contest.setEnrollmentKey(passwordEncoder.encode(contestCreateDTO.getEnrollmentKey()));
        //set author
        var author = userService.getUserEntityById(UUID.fromString(jwtService.getUserIdFromToken())).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );
        contest.setAuthor(author);
        if(contest.getEndTime().isBefore(contest.getStartTime())){
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        var savedContest = contestRepository.save(contest);
        logger.info("Created contest with id: {}", savedContest.getId());
    }


    //ownership needed
    @Transactional
    @Override
    public void deleteById(UUID id) {
        var contest = contestRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id " )
                );
        checkOwnership(contest.getAuthor().getId());
        contestRepository.delete(contest);
        logger.info("Deleted Contest with id: {}", id);

    }

    @Transactional
    @Override
    public void assignProblemsToContest(AddProblemsToContestDTO addProblemsToContestDTO) {
        //find contest
        var contest = contestRepository.findById(UUID.fromString(addProblemsToContestDTO.getContestId()))
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id " )
                );
        //check ownership
        checkOwnership(contest.getAuthor().getId());
        //assign problems
        var problemIds = addProblemsToContestDTO.getProblemIds().stream()
                .map(UUID::fromString)
                .toList();

        problemIds.forEach(problemId -> {
            //find problem
            var problem = problemService.getProblemEntityById(problemId)
                    .orElseThrow(
                            ()-> new ResourceNotFoundException("Problem not found with id " + problemId)
                    );
            //check ownership of the problem
            checkOwnership(problem.getAuthor().getId());
            //save assignment
            contest.getProblems().add(problem);
            logger.info("Assigned Problem with id: {} to Contest with id: {}", problemId, contest.getId());
        });


    }

    public void checkOwnership(UUID authorId) {
        String userId= jwtService.getUserIdFromToken();
        if(!authorId.toString().equals(userId)){
            throw new IllegalArgumentException("You are not the author of this contest");
        }
    }





}
