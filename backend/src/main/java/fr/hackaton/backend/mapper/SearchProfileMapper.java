package fr.hackaton.backend.mapper;

import fr.hackaton.backend.domain.SearchProfile;
import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.dto.searchProfiles.SearchProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {SkillMapper.class})
public interface SearchProfileMapper {

    @Named("toId")
    static String toId(Skill skill) {
        return skill.getId();
    }

    @Named("dateToLocalDate")
    static LocalDate dateToLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    @Mapping(source = "skillList", target = "skillsIds", qualifiedByName = "toId")
    @Mapping(source = "startDate", target = "startDate", qualifiedByName = "dateToLocalDate")
    @Mapping(source = "endDate", target = "endDate", qualifiedByName = "dateToLocalDate")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "dateToLocalDate")
    SearchProfileDTO toDto(SearchProfile searchProfile);

    SearchProfile toEntity(SearchProfileDTO dto);
}
