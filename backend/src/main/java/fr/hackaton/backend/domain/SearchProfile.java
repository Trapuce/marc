package fr.hackaton.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "search_profiles")
@Builder
public class SearchProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String managerId;

    private Integer experience;

    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "search_profile_skills",
        joinColumns = @JoinColumn(name = "search_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @ToString.Exclude
    private List<Skill> skillList;
}