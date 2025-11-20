package com.example.softwarebackend.modules.constest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestCreateDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    @NotBlank(message = "Enrollment key is mandatory")
    @Size(min = 6, message = "Enrollment key must be at least 6 characters long")
    private  String enrollmentKey;

}
