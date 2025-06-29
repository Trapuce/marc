package fr.hackaton.backend.mapper;

import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.dto.employees.CreateEmployeeDto;
import fr.hackaton.backend.dto.employees.EmployeeDTO;
import fr.hackaton.backend.dto.employees.GetEmployeeDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { SkillMapper.class })
public interface EmployeeMapper {

    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee toEntity(EmployeeDTO dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee toEntity(CreateEmployeeDto dto) ;

    GetEmployeeDto employeeToGetEmployeeDto(Employee employee);

   
}
