package com.example.softwarebackend.modules.constest.dto;

import jakarta.validation.constraints.NotBlank;
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

}
