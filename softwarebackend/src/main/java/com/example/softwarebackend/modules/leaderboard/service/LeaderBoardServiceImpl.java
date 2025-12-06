package com.example.softwarebackend.modules.leaderboard.service;

import com.example.softwarebackend.modules.constest.services.ContestService;
import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardEntryResponseDTO;
import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardUpdateDTO;
import com.example.softwarebackend.modules.leaderboard.mapper.LeaderBoardMapper;
import com.example.softwarebackend.modules.leaderboard.repository.LeaderBoardRepository;
import com.example.softwarebackend.modules.user.services.UserService;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import com.example.softwarebackend.shared.entities.LeaderBoardEntry;
import com.example.softwarebackend.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderBoardServiceImpl.class);

    private final LeaderBoardRepository leaderBoardRepository;
    private final ContestService contestService;
    private final UserService userService;

    @Transactional
    @Override
    public void updateLeaderBoard(LeaderBoardUpdateDTO leaderBoardUpdateDTO) {
        // get the existing leaderboard entry if not create new one
        LeaderBoardEntry leaderBoardEntry = leaderBoardRepository
                .findByStudentIdAndContestIdOrderByScoreDesc(leaderBoardUpdateDTO.getStudentId(),leaderBoardUpdateDTO.getContestId())
                .orElseGet(() -> create(leaderBoardUpdateDTO));

        // update the total score
        AtomicReference<Double> updatedTotalScore = new AtomicReference<>(0.0);
        AtomicInteger numberOfProblemSolved = new AtomicInteger();

        var contest = contestService.getContestEntityById(leaderBoardUpdateDTO.getContestId())
                .orElseThrow(() -> new ResourceNotFoundException("Contest not found"));

        var student = userService.getUserEntityById(leaderBoardUpdateDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        //calculate updated total score
        contest.getSubmissions().stream()
                .filter(s -> s.getStudent().getId().equals(student.getId()))
                .forEach(s -> {
                    updatedTotalScore.set(updatedTotalScore.get() + s.getTotalScore());
                    numberOfProblemSolved.getAndIncrement();
                });

        // Update the leaderboard entry
        leaderBoardEntry.setTotalScore(updatedTotalScore.get());
        leaderBoardEntry.setContest(contest);
        leaderBoardEntry.setUser(student);
        leaderBoardEntry.setProblemsSolved(numberOfProblemSolved.get());
        leaderBoardRepository.save(leaderBoardEntry);

        // Recalculate ranks for all participants in this contest
        recalculateRanksForContest(leaderBoardUpdateDTO.getContestId());

        LOGGER.info("Leaderboard updated successfully for student: {} in contest: {}",
                   student.getId(), contest.getId());
    }

    //get leader board by contest id
    @Override
    public PageResponseDTO<LeaderBoardEntryResponseDTO> getLeaderBoardByContestId(String contestId, String search, int page, int size, String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<LeaderBoardEntry> currentPage;
        int currentPageNumber=pageable.getPageNumber();
        List<LeaderBoardEntryResponseDTO> leaderBoardEntryResponseDTOS;
        if (!Objects.equals(search, "") && search != null) {
            currentPage =leaderBoardRepository.findBySearchKeyOrderByScoreDesc(UUID.fromString(contestId), search,pageable);
        } else {
            currentPage = leaderBoardRepository.findAllByContestId(UUID.fromString(contestId),pageable);
        }
        leaderBoardEntryResponseDTOS = (currentPage.getContent()).stream().map(LeaderBoardMapper::toDTO).toList();

        LOGGER.info("Retrieved {} contests", leaderBoardEntryResponseDTOS.size());


        return new PageResponseDTO<>(currentPageNumber,currentPage.getTotalPages(), leaderBoardEntryResponseDTOS);

    }




    /**
     * Recalculates ranks for all participants in a contest based on total score
     * Higher score gets better (lower) rank. In case of tie, more problems solved gets better rank.
     */
    private void recalculateRanksForContest(UUID contestId) {
        // Get all leaderboard entries for this contest, ordered by score descending
        List<LeaderBoardEntry> entries = leaderBoardRepository.findAllByContestIdOrderByScoreDesc(contestId);

        int currentRank = 1;
        double previousScore = -1;
        int previousProblemsSolved = -1;

        for (int i = 0; i < entries.size(); i++) {
            LeaderBoardEntry entry = entries.get(i);

            // If score and problems solved are different from previous entry, update rank
            if (entry.getTotalScore() != previousScore || entry.getProblemsSolved() != previousProblemsSolved) {
                currentRank = i + 1; // Rank is 1-based
            }

            entry.setRank(currentRank);
            previousScore = entry.getTotalScore();
            previousProblemsSolved = entry.getProblemsSolved();
        }

        // Save all updated entries
        leaderBoardRepository.saveAll(entries);

        LOGGER.info("Recalculated ranks for {} participants in contest: {}", entries.size(), contestId);
    }

    public LeaderBoardEntry create(LeaderBoardUpdateDTO leaderBoardUpdateDTO) {
        LeaderBoardEntry leaderBoardEntry = new LeaderBoardEntry();
        var Student = userService.getUserEntityById(leaderBoardUpdateDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student id not found."));
        var Contest = contestService.getContestEntityById(leaderBoardUpdateDTO.getContestId())
                .orElseThrow(() -> new ResourceNotFoundException("Contest id not found."));
        leaderBoardEntry.setTotalScore(0.0);
        leaderBoardEntry.setUser(Student);
        leaderBoardEntry.setContest(Contest);
        leaderBoardEntry.setRank(0); // Will be set when recalculateRanksForContest is called
        return leaderBoardRepository.save(leaderBoardEntry);
    }

}
