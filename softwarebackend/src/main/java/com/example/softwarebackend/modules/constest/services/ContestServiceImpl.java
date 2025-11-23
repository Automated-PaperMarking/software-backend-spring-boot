package com.example.softwarebackend.modules.constest.services;

import com.example.softwarebackend.modules.constest.dto.*;
import com.example.softwarebackend.modules.constest.mapper.ContestMapper;
import com.example.softwarebackend.modules.constest.repository.ContestRepository;
import com.example.softwarebackend.modules.auth.services.JwtService;
import com.example.softwarebackend.modules.problem.dto.ProblemResponseDTO;
import com.example.softwarebackend.modules.problem.mapper.ProblemMapper;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    @Override
    public List<ProblemResponseDTO> getContestProblems(UUID contestId) {
       var contest = contestRepository.findById(contestId)
               .orElseThrow(
                       ()-> new ResourceNotFoundException("Contest not found with id: " + contestId)
               );

       // Get current user
       var currentUser = userService.getUserEntityById(UUID.fromString(jwtService.getUserIdFromToken()))
               .orElseThrow(
                       ()-> new ResourceNotFoundException("User not found")
               );

       // Check contest is started
       OffsetDateTime now = OffsetDateTime.now();
       boolean isContestStarted = now.isAfter(contest.getStartTime());

       if(!isContestStarted){
           // Check if current user is the contest owner
           if(!contest.getAuthor().getId().equals(currentUser.getId())){
               throw new IllegalArgumentException("Contest has not started yet");
           }
       } else {
           // Contest has started - check if user is participant or owner
           boolean isOwner = contest.getAuthor().getId().equals(currentUser.getId());
           boolean isParticipant = contest.getParticipants().contains(currentUser);

           if(!isOwner && !isParticipant){
               throw new IllegalArgumentException("You are not enrolled in this contest");
           }
       }

        return contest.getProblems()
                .stream()
                .map(ProblemMapper::toDTO)
                .toList();
    }

    //enroll to contest
    @Transactional
    @Override
    public void enrollToContest(ContestEnrollRequestDTO contestEnrollRequestDTO) {
        var contest = contestRepository.findById(UUID.fromString(contestEnrollRequestDTO.getContestId()))
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id " )
                );
        //get current user
        var currentUser = userService.getUserEntityById(UUID.fromString(jwtService.getUserIdFromToken()))
                .orElseThrow(
                        ()-> new ResourceNotFoundException("User not found")
                );
        //check if user is already enrolled
        if(contest.getParticipants().contains(currentUser)){
            throw new IllegalArgumentException("User is already enrolled in the contest");
        }
        //check enrollment key
        if(!passwordEncoder.matches(contestEnrollRequestDTO.getEnrollmentKey(), contest.getEnrollmentKey())){
            throw new IllegalArgumentException("Invalid enrollment key");
        }
        //enroll user
        currentUser.getParticipatedContests().add(contest);
        contest.getParticipants().add(currentUser);
        userService.saveUserEntity(currentUser);
        logger.info("User with id: {} enrolled to contest with id: {}", currentUser.getId(), contest.getId());
    }


    @Override
    public Optional<Contest> getContestEntityById(UUID uuid) {
        return  contestRepository.findById(uuid);
    }


    /// ownership need section //////////////

    @Transactional
    @Override
    public void deleteById(UUID id) {
        var contest = contestRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Contest not found with id " )
                );

        //check ownership
        String currentUserId = jwtService.getUserIdFromToken();
        contest.verifyOwner(UUID.fromString(currentUserId));

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
        String currentUserId = jwtService.getUserIdFromToken();
        contest.verifyOwner(UUID.fromString(currentUserId));
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
            problem.verifyOwner(UUID.fromString(currentUserId));
            if(contest.getProblems().contains(problem)){
                throw new IllegalArgumentException("Problem is already assigned to contest " );
            }
            //save assignment
            contest.getProblems().add(problem);
            logger.info("Assigned Problem with id: {} to Contest with id: {}", problemId, contest.getId());
        });

        //save the contest to persist the problem assignments
        contestRepository.save(contest);
        logger.info("Successfully assigned {} problems to contest with id: {}", problemIds.size(), contest.getId());

    }

    @Transactional
    @Override
    public void removeProblemFromContest(RemoveProblemsToContestDTO removeProblemsToContestDTO) {

        Contest contest = contestRepository.findById(UUID.fromString(removeProblemsToContestDTO.getContestId()))
                .orElseThrow(() -> new ResourceNotFoundException("Contest not found"));

        // Check ownership
        String currentUserId = jwtService.getUserIdFromToken();
        contest.verifyOwner(UUID.fromString(currentUserId));
        List<UUID> problemIds = removeProblemsToContestDTO.getProblemIds().stream()
                .map(UUID::fromString)
                .toList();
        for(UUID problemId : problemIds){
            var problem = problemService.getProblemEntityById(problemId)
                    .orElseThrow(
                            ()-> new ResourceNotFoundException("Problem not found with id " )
                    );
            //check ownership of the problem
            problem.verifyOwner(UUID.fromString(currentUserId));
            //check if problem is assigned to contest
            if(!contest.getProblems().contains(problem)){
                throw new IllegalArgumentException("Problem is not assigned to contest");
            }
            //remove problem from contest
            contest.getProblems().remove(problem);
            problem.getContests().remove(contest);
            logger.info("Successfully removed {} problems to contest with id: {}", problemIds.size(), contest.getId());

        }

        //save the contest to persist the changes
        contestRepository.save(contest);
        logger.info("Successfully removed {} problems from contest with id: {}", problemIds.size(), contest.getId());

    }



}
