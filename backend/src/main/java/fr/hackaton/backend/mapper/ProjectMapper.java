package fr.hackaton.backend.mapper;

import org.mapstruct.Mapper;

import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.domain.Project;
import fr.hackaton.backend.dto.projects.ProjectDTO;

import org.mapstruct.Mapping;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface ProjectMapper {

    @Mapping(source = "employees", target = "employeesIds")
    ProjectDTO toDto(Project project);

    @Mapping(source = "employeesIds", target = "employees", ignore = true) 
    Project toEntity(ProjectDTO dto);

    default List<UUID> mapEmployeesToIds(List<Employee> employees) {
        if (employees == null) return null;
        return employees.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());
    }

    Iterable<ProjectDTO> toProjectDtos(Iterable<Project> projects);
    Iterable<Project> toProjects(Iterable<ProjectDTO> projectDtos);
}
