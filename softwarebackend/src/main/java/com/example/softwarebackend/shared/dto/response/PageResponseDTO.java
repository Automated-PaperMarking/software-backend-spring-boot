package com.example.softwarebackend.shared.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO<T> {

    private int page;
    private int totalPages;
    private List<T> data;

}
