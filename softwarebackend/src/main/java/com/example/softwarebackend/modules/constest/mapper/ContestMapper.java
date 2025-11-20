package com.example.softwarebackend.modules.constest.mapper;

import com.example.softwarebackend.modules.constest.dto.ContestCreateDTO;
import com.example.softwarebackend.modules.constest.dto.ContestResponseDTO;
import com.example.softwarebackend.shared.entities.Contest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;

public class ContestMapper {

    public static Contest toEntity(ContestCreateDTO dto) {
        Contest contest = new Contest();
        contest.setName(dto.getName());
        contest.setDescription(dto.getDescription());
        contest.setStartTime(OffsetDateTime.parse(dto.getStartTime()));
        contest.setEndTime(OffsetDateTime.parse(dto.getEndTime()));
        return contest;
    }
    public static ContestResponseDTO toDTO(Contest contest) {
        return ContestResponseDTO.builder()
                .id(contest.getId().toString())
                .name(contest.getName())
                .description(contest.getDescription())
                .createdAt(contest.getCreatedAt().toString())
                .updatedAt(contest.getUpdatedAt().toString())
                .endTime(contest.getEndTime()!=null ? contest.getEndTime().toString() :"")
                .startTime(contest.getStartTime() != null ? contest.getStartTime().toString():"" )
                .build();
    }
}
