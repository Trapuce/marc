package fr.hackaton.backend.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fr.hackaton.backend.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
    List<Project> findByManagerId(String managerId);
    List<Project> findByEmployeesId(UUID employees_id);
    List<Project> findByArchivedAtIsNull();
    List<Project> findByEndDateBefore(Date date);
    List<Project> findByStartDateBetween(Date startDate, Date endDate);
}
