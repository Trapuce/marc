package fr.hackaton.backend.mapper;

import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.dto.skills.SkillDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillDTO toDto(Skill skill);
    Skill toEntity(SkillDTO dto);
    
    default List<String> map(List<Skill> skills) {
        return skills.stream()
            .map(Skill::getId)
            .collect(Collectors.toList());
    }
}
