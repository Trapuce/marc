package fr.hackaton.backend.dto.employees;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Data;


@Data
public class GetEmployeeDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String urlCV;
    private LocalDate careerStart;
    private LocalDate createdAt;
    private List<String> skills;
    
}
