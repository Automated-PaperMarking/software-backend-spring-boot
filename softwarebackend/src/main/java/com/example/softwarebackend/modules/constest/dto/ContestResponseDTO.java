package com.example.softwarebackend.modules.constest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContestResponseDTO {
    private String id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String createdAt;
    private String updatedAt;

}
