package fr.hackaton.backend.rest;

import fr.hackaton.backend.common.PageResponse;
import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.dto.employees.GetEmployeeDto;
import fr.hackaton.backend.dto.skills.SkillDTO;
import fr.hackaton.backend.mapper.SkillMapper;
import fr.hackaton.backend.repository.SkillRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RestController
@RequestMapping("/skills")
@Tag(name = "Skills", description = "API to manage skills.")
public class SkillsResource {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    /**
     * Get all skills.
     * Paginated query
     * @return A list of skills.
     */
    @Operation(summary = "Get all skills", description = "Retrieve a list of all skills available.")
    @GetMapping("")
    public ResponseEntity<List<SkillDTO>> all() {
        List<Skill> skills = skillRepository.findAll();
        List<SkillDTO> dtoList = skills.stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}
