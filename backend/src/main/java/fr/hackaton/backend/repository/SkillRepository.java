package fr.hackaton.backend.repository;

import fr.hackaton.backend.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, String> {
    Optional<Skill> findByLabel(String label);
}