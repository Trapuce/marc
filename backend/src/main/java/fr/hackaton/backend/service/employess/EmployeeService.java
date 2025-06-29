package fr.hackaton.backend.service.employess;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import fr.hackaton.backend.domain.Project;
import fr.hackaton.backend.dto.projects.ProjectDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.hackaton.backend.common.PageResponse;
import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.domain.Project;
import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.dto.employees.CVDownloadResponse;
import fr.hackaton.backend.dto.employees.CreateEmployeeDto;
import fr.hackaton.backend.dto.employees.EmployeeDTO;
import fr.hackaton.backend.dto.employees.GetEmployeeDto;
import fr.hackaton.backend.dto.projects.ProjectDTO;
import fr.hackaton.backend.dto.skills.SkillDTO;
import fr.hackaton.backend.exceptions.EmployeeNotFoundException;
import fr.hackaton.backend.mapper.EmployeeMapper;
import fr.hackaton.backend.repository.EmployeeRepository;
import fr.hackaton.backend.repository.ProjectRepository;
import fr.hackaton.backend.repository.SkillRepository;
import fr.hackaton.backend.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {

    private static final String EMPLOYEES_CACHE = "employees";
    private static final String EMPLOYEE_BY_ID_CACHE = "employeeById";
    private static final String FILTERED_EMPLOYEES_CACHE = "filteredEmployees";

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final StorageService storageService;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final RepositoryMethodInvocationListener repositoryMethodInvocationListener;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
            StorageService storageService, SkillRepository skillRepository, ProjectRepository projectRepository,
            RepositoryMethodInvocationListener repositoryMethodInvocationListener) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.storageService = storageService;
        this.skillRepository = skillRepository;
        this.projectRepository = projectRepository;
        this.repositoryMethodInvocationListener = repositoryMethodInvocationListener;
    }

    public CVDownloadResponse generateCVDownloadUrl(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        if (employee.getUrlCV() == null) {
            throw new IllegalArgumentException("No CV found for this employee");
        }

        CVDownloadResponse response = new CVDownloadResponse();
        response.setDownloadUrl(employee.getUrlCV());
        response.setExpiresIn(15 * 60L);
        response.setFilename(this.storageService.extractFilename(employee.getUrlCV()));

        return response;
    }

    @CacheEvict(value = { EMPLOYEES_CACHE, FILTERED_EMPLOYEES_CACHE }, allEntries = true)
    public EmployeeDTO createEmployee(CreateEmployeeDto employeeDTO, MultipartFile cvFile) {
        String cvUrl = null;
        if (cvFile != null && !cvFile.isEmpty()) {
            cvUrl = storageService.uploadCV(cvFile);
        }
        Employee newEmployee = employeeMapper.toEntity(employeeDTO);
        newEmployee.setCreatedAt(LocalDate.now());
        newEmployee.setUrlCV(cvUrl);

        if (employeeDTO.getSkills() != null && !employeeDTO.getSkills().isEmpty()) {
            for (SkillDTO skillDTO : employeeDTO.getSkills()) {
                Skill skill = skillRepository.findById(skillDTO.getId())
                        .orElseGet(() -> {
                            Skill newSkill = new Skill();
                            newSkill.setId(skillDTO.getId());
                            newSkill.setLabel(skillDTO.getLabel());
                            return skillRepository.save(newSkill);
                        });
                newEmployee.addSKill(skill);
            }
        }

        Employee savedEmployee = employeeRepository.save(newEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Cacheable(value = EMPLOYEE_BY_ID_CACHE, key = "#id")
    public GetEmployeeDto getEmployee(UUID id) {
        log.debug("Fetching employee from database for id: {}", id);
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return this.employeeMapper.employeeToGetEmployeeDto(employee);
    }

    @Cacheable(value = FILTERED_EMPLOYEES_CACHE)
    public PageResponse<GetEmployeeDto> getFilteredEmployees(
            List<String> skills,
            LocalDate from,
            LocalDate to,
            Integer experience,
            String firstName,
            String lastName,
            Pageable pageable) {
        log.debug("Fetching filtered employees from database");

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                !skills.isEmpty() ? skills.toArray(new String[0]) : null,
                from,
                to,
                experience,
                firstName,
                lastName);
                
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredEmployees.size());
        Page<Employee> employeePage = new PageImpl<>(filteredEmployees.subList(start, end), pageable,
                filteredEmployees.size());

        Page<GetEmployeeDto> dtoPage = employeePage.map(employeeMapper::employeeToGetEmployeeDto);

        return new PageResponse<>(dtoPage);
    }

    @Cacheable(value = EMPLOYEES_CACHE, key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<GetEmployeeDto> getAllEmployees(Pageable pageable) {
        log.debug("Fetching all employees from database");
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employeeMapper::employeeToGetEmployeeDto);
    }

    @CachePut(value = EMPLOYEE_BY_ID_CACHE, key = "#id")
    @CacheEvict(value = { EMPLOYEES_CACHE, FILTERED_EMPLOYEES_CACHE }, allEntries = true)
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) throws BadRequestException {

        Employee employeeToUpdate = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if (employeeDTO.getFirstname() != null) {
            employeeToUpdate.setFirstname(employeeDTO.getFirstname());
        }
        if (employeeDTO.getLastname() != null) {
            employeeToUpdate.setLastname(employeeDTO.getLastname());
        }
        if (employeeDTO.getEmail() != null) {
            employeeToUpdate.setEmail(employeeDTO.getEmail());
        }
        if (employeeDTO.getUrlCV() != null) {
            employeeToUpdate.setUrlCV(employeeDTO.getUrlCV());
        }
        if (employeeDTO.getCareerStart() != null) {
            employeeToUpdate.setCareerStart(employeeDTO.getCareerStart());
        }

        if (employeeDTO.getSkills() != null) {
            List<Skill> skills = new ArrayList<>();

            for (SkillDTO skillDTO : employeeDTO.getSkills()) {
                Skill skill = skillRepository.findById(skillDTO.getId())
                        .orElseThrow(() -> new BadRequestException("Skill not found: " + skillDTO.getId()));
                skills.add(skill);
            }

            employeeToUpdate.setSkills(skills);
        }

        if (employeeDTO.getProjects() != null) {
            List<Project> projects = new ArrayList<>();

            for (ProjectDTO projectDTO : employeeDTO.getProjects()) {
                Project project = projectRepository.findById(projectDTO.getId())
                        .orElseThrow(() -> new BadRequestException("Project not found: " +
                                projectDTO.getId()));
                projects.add(project);
            }

            employeeToUpdate.setProjects(projects);
        }

        Employee updatedEmployee = this.employeeRepository.save(employeeToUpdate);

        return this.employeeMapper.toDto(updatedEmployee);
    }

    @CacheEvict(value = { EMPLOYEES_CACHE, EMPLOYEE_BY_ID_CACHE, FILTERED_EMPLOYEES_CACHE }, allEntries = true)
    @Transactional
    public void deleteEmployee(UUID id) {
        Employee employeeToDelete = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        Set<Skill> skillsToRemove = new HashSet<>(employeeToDelete.getSkills());
        for (Skill skill : skillsToRemove) {
            employeeToDelete.removeSkill(skill);
        }

        Set<Project> projectsToRemove = new HashSet<>(employeeToDelete.getProjects());
        for (Project project : projectsToRemove) {
            employeeToDelete.removeProject(project);
        }

        employeeRepository.save(employeeToDelete);

        employeeRepository.delete(employeeToDelete);
    }

}
