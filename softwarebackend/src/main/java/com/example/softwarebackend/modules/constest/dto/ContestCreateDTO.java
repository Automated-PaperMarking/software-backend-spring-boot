package com.example.softwarebackend.modules.constest.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestCreateDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 255, message = "Name must be at most 255 characters long")
    private String name;
    @Size(max = 1000, message = "Description must be at most 1000 characters long")
    private String description;
    @NotBlank(message = "Enrollment key is mandatory")
    @Size(min = 6, message = "Enrollment key must be at least 6 characters long")
    private  String enrollmentKey;
    @NotBlank(message = "Start time is mandatory")
    private String startTime;
    private String endTime;

}
