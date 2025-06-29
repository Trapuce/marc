package fr.hackaton.backend.dto.projects;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProjectDTO {
    private UUID id;
    private String name;
    private String description;
    private String color;
    private String managerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String client;
    private LocalDate archivedAt;
    private LocalDate createdAt;
    private List<UUID> employeesIds;
}
