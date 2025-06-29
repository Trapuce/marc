package fr.hackaton.backend.service;

import fr.hackaton.backend.domain.SearchProfile;
import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.dto.searchProfiles.SearchProfileDTO;
import fr.hackaton.backend.mapper.SearchProfileMapper;
import fr.hackaton.backend.repository.SearchProfileRepository;
import fr.hackaton.backend.repository.SkillRepository;
import fr.hackaton.backend.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SearchProfileService {

    @Autowired
    private SearchProfileRepository searchProfileRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SearchProfileMapper searchProfileMapper;
    @Autowired
    private SecurityUtils securityUtils;

    /**
     * Creates a new search profile for a given manager.
     *
     * @param searchProfileDTO The DTO containing the search profile data.
     * @return The created search profile as a DTO.
     * @throws NoSuchElementException if a specified skill does not exist.
     */
    public SearchProfileDTO createSearchProfile(SearchProfileDTO searchProfileDTO) {
        String managerId = securityUtils.getCurrentUserId();
        SearchProfile searchProfile = new SearchProfile();
        searchProfile.setName(searchProfileDTO.getName());
        searchProfile.setManagerId(managerId);
        searchProfile.setExperience(searchProfileDTO.getExperience());
        searchProfile.setStartDate(searchProfileDTO.getStartDate());
        searchProfile.setEndDate(searchProfileDTO.getEndDate());
        List<Skill> skills = searchProfileDTO.getSkillsIds().stream().map(id -> this.skillRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Skill not found with ID: " + id))).toList();

        searchProfile.setSkillList(skills);

        return this.searchProfileMapper.toDto(this.searchProfileRepository.save(searchProfile));
    }

    /**
     * @return
     */
    public List<SearchProfileDTO> getAllProfiles() {
        String managerId = securityUtils.getCurrentUserId();
        return this.searchProfileRepository.findAllByManagerId(managerId).stream().map(searchProfileMapper::toDto).toList();
    }
}
