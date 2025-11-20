package com.example.softwarebackend.shared.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="contest_problems",
            joinColumns = @JoinColumn(name="contest_id"),
            inverseJoinColumns = @JoinColumn(name="problem_id")
    )
    @JsonManagedReference
    private List<Problem> problems;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private User author;

    @ManyToMany(mappedBy = "participatedContests")
    @JsonBackReference
    private List<User> participants;


    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
