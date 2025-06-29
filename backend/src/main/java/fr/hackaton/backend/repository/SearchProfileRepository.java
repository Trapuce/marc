package fr.hackaton.backend.repository;

import fr.hackaton.backend.domain.SearchProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;
import java.util.UUID;

@Repository
public interface SearchProfileRepository extends JpaRepository<SearchProfile, UUID> {
    List<SearchProfile> findByEndDateAfter(Date date);
    List<SearchProfile> findByStartDateBefore(Date date);
    List<SearchProfile> findByExperienceGreaterThanEqual(Integer experience);

    List<SearchProfile> findAllByManagerId(@Param("managerId") String managerId);
}