package com.example.softwarebackend.modules.constest.mapper;

import com.example.softwarebackend.modules.constest.dto.ContestCreateDTO;
import com.example.softwarebackend.modules.constest.dto.ContestResponseDTO;
import com.example.softwarebackend.shared.entities.Contest;

public class ContestMapper {

    public static Contest toEntity(ContestCreateDTO dto) {
        Contest contest = new Contest();
        contest.setName(dto.getName());
        contest.setDescription(dto.getDescription());
        return contest;
    }
    public static ContestResponseDTO toDTO(Contest contest) {
        return ContestResponseDTO.builder()
                .id(contest.getId().toString())
                .name(contest.getName())
                .description(contest.getDescription())
                .build();
    }
}
