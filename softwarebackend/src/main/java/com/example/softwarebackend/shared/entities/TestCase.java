package com.example.softwarebackend.shared.entities;

import com.example.softwarebackend.shared.enums.TestCaseType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Profile;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String input;
    @Column(nullable = false)
    private String expectedOutput;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TestCaseType type;
    @ManyToOne
    @JoinColumn(name = "problem_id")
    @JsonBackReference
    private Problem problem;
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
