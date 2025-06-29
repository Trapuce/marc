package fr.hackaton.backend.rest;

import fr.hackaton.backend.common.PageResponse;
import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.dto.employees.CVDownloadResponse;
import fr.hackaton.backend.dto.employees.CreateEmployeeDto;
import fr.hackaton.backend.dto.employees.EmployeeDTO;
import fr.hackaton.backend.dto.employees.GetEmployeeDto;
import fr.hackaton.backend.dto.skills.SkillDTO;
import fr.hackaton.backend.service.CVProcessingService;
import fr.hackaton.backend.service.employess.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RestController
@RequestMapping("/employees")
@Tag(name = "Employees", description = "API to manage employees.")
public class EmployeesResource {

    private final EmployeeService employeeService;

    private final CVProcessingService cvProcessingService;

    public EmployeesResource(EmployeeService employeeService, CVProcessingService cvProcessingService) {
        this.employeeService = employeeService;
        this.cvProcessingService = cvProcessingService;
    }

    /**
     * Get all employees filtered.
     * Paginated query.
     *
     * @param firstName
     * @param lastName
     * @param skills
     * @param exp
     * @param from
     * @param to
     * @param page
     * @param size
     * @return A list of Employees.
     */
    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees available.")
    @GetMapping("")
    public PageResponse<GetEmployeeDto> all(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false, defaultValue = "") List<String> skills,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        return employeeService.getFilteredEmployees(
                skills,
                from,
                to,
                experience,
                firstName,
                lastName,
                PageRequest.of(page, size));

    }

    /**
     * Get a specific employee by its ID.
     *
     * @param id The ID of the employee to retrieve.
     * @return The EmployeeDTO of the requested employee.
     */
    @Operation(summary = "Get an employee by ID", description = "Retrieve a specific employee by providing its ID.")
    @GetMapping("/{id}")
    public GetEmployeeDto one(@PathVariable UUID id) {
        return this.employeeService.getEmployee(id);
    }

    /**
     * Delete an employee by its ID.
     *
     * @param id The ID of the employee to delete.
     */
    @Operation(summary = "Delete an employee", description = "Delete a employee by providing its ID.")
    @DeleteMapping("/{id}")
    public void deleteSessionById(@PathVariable UUID id) {
        this.employeeService.deleteEmployee(id);

    }

    @Operation(summary = "Get employee CV", description = "Get a presigned URL to download employee CV")
    @GetMapping("/{employeeId}/cv")
    public ResponseEntity<CVDownloadResponse> getEmployeeCV(@PathVariable UUID employeeId) {
        CVDownloadResponse response = employeeService.generateCVDownloadUrl(employeeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create an employee", description = "Create an employee by providing employee data and CV file.")
    @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EmployeeDTO> createEmployee(
            @RequestPart(value = "employee") String employeeJson,
            @RequestPart(value = "cvFile" , required = false) MultipartFile cvFile) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        CreateEmployeeDto employeeDTO = mapper.readValue(employeeJson, CreateEmployeeDto.class);

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO, cvFile);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdEmployee);
    }

    @PostMapping("/extract-cv")
    public ResponseEntity<List<SkillDTO>> extractCVInfo(
            @RequestPart(value = "cvFile") MultipartFile cvFile) {
        List<SkillDTO> skills = cvProcessingService.extractSkillsFromCV(cvFile);

        return ResponseEntity.ok(skills);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable String id,
            @RequestBody EmployeeDTO employeeDTO) throws BadRequestException {
        UUID employeeId;
        try {
            employeeId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format: " + id);
        }

        employeeDTO.setId(employeeId);
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeId, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
}