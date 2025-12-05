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
                .findByStudentIdAndContestId(leaderBoardUpdateDTO.getStudentId(),leaderBoardUpdateDTO.getContestId())
                .orElseGet(() -> create(leaderBoardUpdateDTO));

        // update the total score
        double updatedTotalScore = leaderBoardEntry.getTotalScore() + leaderBoardUpdateDTO.getTotalScoreForSubmission();

        // NEED TO IMPLEMENT LOGIC TO DO THE RANKING BASED ON TOTAL SCORE

        var contest = contestService.getContestEntityById(leaderBoardUpdateDTO.getContestId())
                .orElseThrow(() -> new ResourceNotFoundException("Contest not found"));

        var student = userService.getUserEntityById(leaderBoardUpdateDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        leaderBoardEntry.setTotalScore(updatedTotalScore);
        leaderBoardEntry.setContest(contest);
        leaderBoardEntry.setUser(student);
        leaderBoardRepository.save(leaderBoardEntry);
        LOGGER.info("Leaderboard updated successfully.");
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
            currentPage =leaderBoardRepository.findBySearchKey(UUID.fromString(contestId), search,pageable);
        } else {
            currentPage = leaderBoardRepository.findAllByContestId(UUID.fromString(contestId),pageable);
        }
        leaderBoardEntryResponseDTOS = (currentPage.getContent()).stream().map(LeaderBoardMapper::toDTO).toList();

        LOGGER.info("Retrieved {} contests", leaderBoardEntryResponseDTOS.size());


        return new PageResponseDTO<LeaderBoardEntryResponseDTO>(currentPageNumber,currentPage.getTotalPages(), leaderBoardEntryResponseDTOS);

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
        return leaderBoardRepository.save(leaderBoardEntry);
    }

}
