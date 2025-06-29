

package fr.hackaton.backend.repository;

import fr.hackaton.backend.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findBySkillsId(String skillsId);

    @Query(value = "SELECT * FROM find_filtered_employees(:skills, :start_date, :end_date, :experience, :firstname, :lastname)", nativeQuery = true)
    List<Employee> findFilteredEmployees(
            @Param("skills") String[] skills,
            @Param("start_date") LocalDate startDate,
            @Param("end_date") LocalDate endDate,
            @Param("experience") Integer experience,
            @Param("firstname") String firstName,
            @Param("lastname") String lastName
    );

    @Query(value = "SELECT is_employee_available(:employeeId, :startDate, :endDate)", nativeQuery = true)
    Boolean isEmployeeAvailable(
            @Param("employeeId") UUID employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<Employee> findByFirstname(String firstname);
}
