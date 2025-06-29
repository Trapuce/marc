package fr.hackaton.backend.rest;

import fr.hackaton.backend.dto.searchProfiles.SearchProfileDTO;
import fr.hackaton.backend.service.SearchProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@RestController
@RequestMapping("/searchProfiles")
@Tag(name = "SearchProfiles", description = "API to manage search profiles for managers.")
public class SearchProfilesResource {

    @Autowired
    private SearchProfileService searchProfileService;

    /**
     * Get all search profiles saved by the user (manager).
     * Uses JWT user id as a manager id.
     * Access to this must be granted only to managers.
     *
     * @return the list of all search profiles.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get all search profiles for manager.", description = "Retrieve a list of all search profiles previously saved by the manager by using his id.")
    @GetMapping(value = "", produces = "application/json")
    public List<SearchProfileDTO> all() {
        return this.searchProfileService.getAllProfiles();
    }

    /**
     * Create a search profile.
     * Uses JWT user id as a manager id.
     * Access to this must be granted only to managers.
     *
     * @param searchProfileDTO
     * @return the employee newly created.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a search profile", description = "Save a search profile for a manager.")
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    public SearchProfileDTO create(@RequestBody SearchProfileDTO searchProfileDTO) {
        return this.searchProfileService.createSearchProfile(searchProfileDTO);
    }
}