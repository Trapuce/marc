package fr.hackaton.backend.service.projects;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.hackaton.backend.dto.projects.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.domain.Project;
import fr.hackaton.backend.mapper.ProjectMapper;
import fr.hackaton.backend.repository.EmployeeRepository;
import fr.hackaton.backend.repository.ProjectRepository;
import fr.hackaton.backend.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final SecurityUtils securityUtils;
    private final EmployeeRepository employeeRepository;

    
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.securityUtils = new SecurityUtils();
        this.employeeRepository = employeeRepository;

    }

    public Optional<ProjectDTO> getProjectById(UUID id) {
        return projectRepository.findById(id).map(projectMapper::toDto);
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        String currentUserId = securityUtils.getCurrentUserId();
        project.setManagerId(currentUserId);
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setColor(projectDTO.getColor());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setClient(projectDTO.getClient());

        
        if (projectDTO.getEmployeesIds() != null && !projectDTO.getEmployeesIds().isEmpty()) {
            for (UUID employeeId : projectDTO.getEmployeesIds()) {
                boolean isAvailable = employeeRepository.isEmployeeAvailable(employeeId, projectDTO.getStartDate(), projectDTO.getEndDate());
                log.info("Vérification disponibilité pour employé {} : {} pour la période du {} au {}", 
                    employeeId, 
                    isAvailable, 
                    projectDTO.getStartDate(), 
                    projectDTO.getEndDate()
                );
                
                if (!isAvailable) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'employé " + employeeId + " n'est pas disponible pour les dates du projet");
                }
            }
            
            
            // Si tous les employés sont disponibles, les ajouter au projet
            List<Employee> employees = employeeRepository.findAllById(projectDTO.getEmployeesIds());
            employees.forEach(project::addEmployee);
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    @Transactional
    public Optional<ProjectDTO> updateProject(UUID id, ProjectDTO projectDTO) {
        return projectRepository.findById(id).map(existingProject -> {
        
            String currentUserId = securityUtils.getCurrentUserId();
            if (!existingProject.getManagerId().equals(currentUserId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à modifier ce projet");
            }

            Project updatedProject = projectMapper.toEntity(projectDTO);

            updatedProject.setName(projectDTO.getName());
            updatedProject.setDescription(projectDTO.getDescription());
            updatedProject.setColor(projectDTO.getColor());
            updatedProject.setClient(projectDTO.getClient());

            List<Employee> employees = employeeRepository.findAllById(projectDTO.getEmployeesIds());
            updatedProject.setEmployees(employees);

            Project savedProject = projectRepository.save(updatedProject);
            return projectMapper.toDto(savedProject);
        });
    }

    @Transactional
    public ProjectDTO archiveProject(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Projet non trouvé"));
            
        String currentUserId = securityUtils.getCurrentUserId();
        if (!project.getManagerId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à archiver ce projet");
        }
        
        if (project.getArchivedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le projet est déjà archivé");
        }
        
        project.setArchivedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return projectMapper.toDto(projectRepository.save(project));
    }

    public Page<ProjectDTO> getProjectsByManager(Boolean archived, Pageable pageable) {
        String currentUserId = securityUtils.getCurrentUserId();
        
        Specification<Project> spec = (root, query, cb) -> 
            cb.equal(root.get("managerId"), currentUserId);
        
        if (archived != null) {
            spec = spec.and((root, query, cb) -> {
                if (archived) {
                    return cb.isNotNull(root.get("archivedAt"));
                } else {
                    return cb.isNull(root.get("archivedAt"));
                }
            });
        }
        
        Page<Project> projectPage = projectRepository.findAll(spec, pageable);
        return projectPage.map(projectMapper::toDto);
    }
}
