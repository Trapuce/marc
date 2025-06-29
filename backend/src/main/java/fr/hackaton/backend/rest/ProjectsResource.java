package fr.hackaton.backend.rest;

import java.util.UUID;

import fr.hackaton.backend.dto.projects.ProjectDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.hackaton.backend.common.PageResponse;
import fr.hackaton.backend.service.projects.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Component
@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "API to manage projects.")
public class ProjectsResource {

    private final ProjectService projectService;

    @Autowired
    public ProjectsResource(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Get all projects for the manager.
     * Uses JWT user id as the manager id to filter out results.
     * Access to this must be granted only to managers.
     *
     * @param archived
     * @return the list of all projects.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get projects for current manager",
            description = "Retrieve all projects managed by the current user (identified by JWT token)")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<PageResponse<ProjectDTO>> all(@RequestParam(required = false) Boolean archived,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int limit) {

        Pageable pageable = PageRequest.of(page, limit);
        Page<ProjectDTO> projects = projectService.getProjectsByManager(archived, pageable);
        return ResponseEntity.ok(new PageResponse<>(projects));
    }

    /**
     * Get a specific project by its ID.
     * Access to this must be granted only to managers.
     *
     * @param id The ID of the project to retrieve.
     * @return The ProjectDTO of the requested project.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get a project by ID", description = "Retrieve a specific project by providing its ID.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProjectDTO> one(@PathVariable UUID id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    /**
     * Create a project.
     * Uses JWT user id as the manager id for newly created project.
     * Access to this must be granted only to managers.
     *
     * @param projectDTO
     * @return the project newly created.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a project", description = "Create a project by providing the object in body.")
    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    /**
     * Update a project.
     * Access to this must be granted only to managers.
     *
     * @param projectDTO
     * @return the project newly created.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update a project", description = "Update an existing project by providing the object in body.")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProjectDTO> update(@PathVariable String id,@RequestBody ProjectDTO projectDTO) throws BadRequestException{
        UUID projectId;
        try {
            projectId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format: " + id);
        }

        projectDTO.setId(projectId);

        return projectService.updateProject(projectDTO.getId(), projectDTO)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projet non trouvé"));
    }

    /**
     * Archive a project.
     * Access to this must be granted only to managers.
     *
     * @return the archived project.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Archive a project", description = "Set an existing project as archived.")
    @PostMapping(value = "/{id}/archive",  produces = "application/json")
    public ResponseEntity<ProjectDTO> archive(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.archiveProject(id));
    }
}