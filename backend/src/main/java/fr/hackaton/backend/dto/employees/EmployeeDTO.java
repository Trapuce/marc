package fr.hackaton.backend.dto.employees;

import fr.hackaton.backend.dto.projects.ProjectDTO;
import fr.hackaton.backend.dto.skills.SkillDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import java.time.LocalDate;


@Data
public class EmployeeDTO {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String urlCV;
    private LocalDate careerStart;
    private LocalDate createdAt;
    private List<SkillDTO> skills;
    private List<ProjectDTO> projects;
}
