package fr.hackaton.backend.dto.searchProfiles;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SearchProfileDTO {
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private Integer experience;
    private LocalDate createdAt;
    private List<String> skillsIds;
}
