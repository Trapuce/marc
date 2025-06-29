package fr.hackaton.backend.repository;

import fr.hackaton.backend.domain.Employee;
import fr.hackaton.backend.domain.Project;
import fr.hackaton.backend.domain.SearchProfile;
import fr.hackaton.backend.domain.Skill;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @BeforeEach
    public void cleanUp() {
        for (Employee employee : employeeRepository.findAll()) {
            employee.setProjects(new ArrayList<>());
            employee.setSkills(new ArrayList<>());
            employeeRepository.save(employee);
        }
        for (Project project : projectRepository.findAll()) {
            project.setEmployees(new ArrayList<>());
            projectRepository.save(project);
        }
        for (Skill skill : skillRepository.findAll()) {
            skill.setEmployees(new ArrayList<>());
            skillRepository.save(skill);
        }
        for (SearchProfile searchProfile : searchProfileRepository.findAll()) {
            searchProfile.setSkillList(new ArrayList<>());
            searchProfileRepository.save(searchProfile);
        }

        projectRepository.deleteAll();
        skillRepository.deleteAll();
        searchProfileRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SearchProfileRepository searchProfileRepository;

    private Skill createAndSaveSkill(String label) {
        Skill skill = new Skill();
        skill.setId(label.toLowerCase());
        skill.setLabel(label);
        skillRepository.save(skill);
        return skillRepository.findByLabel(label).orElseThrow();
    }

    private Project createAndSaveProject(String name, LocalDate startDate, LocalDate endDate) {
        Project project = new Project().builder()
                .name(name)
                .description("Test Description")
                .managerId("Manager123")
                .startDate(startDate)
                .endDate(endDate)
                .build();
        projectRepository.save(project);
        return project;
    }

    private Employee createAndSaveEmployee(String firstname, String lastname, LocalDate careerStart, List<Skill> skills, List<Project> projects) {
        Employee employee = new Employee().builder()
                .firstname(firstname)
                .lastname(lastname)
                .careerStart(careerStart)
                .urlCV("test")
                .skills(new ArrayList<>(skills))
                .projects(new ArrayList<>(projects)).build();
        employeeRepository.save(employee);
        return employee;
    }

    private String getSkillIdByLabel(String label) {
        return skillRepository.findByLabel(label)
                .map(Skill::getId)
                .orElse(null);
    }


    @Test
    void testFindFilteredEmployees() {
        Skill skill = createAndSaveSkill("Java");
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10));
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), List.of(skill), List.of(project));
        skill.setEmployees(employeeRepository.findAll());
        skillRepository.save(skill);
        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 19);
        LocalDate endDate = LocalDate.of(2025, 2, 20);
        Integer experience = 3;
        String firstName = "John";
        String lastName = "Doe";

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                new String[]{getSkillIdByLabel("Java")},
                startDate,
                endDate,
                experience,
                firstName,
                lastName
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesBySkill() {
        Skill skill = createAndSaveSkill("Java");
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), List.of(skill), new ArrayList<>());
        skill.setEmployees(employeeRepository.findAll());
        skillRepository.save(skill);

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                new String[]{getSkillIdByLabel("Java")},
                null,
                null,
                null,
                null,
                null
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesByExperience() {
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), new ArrayList<>());

        Integer experience = 3;

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                null,
                null,
                null,
                experience,
                null,
                null
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesByFirstName() {
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), new ArrayList<>());

        String firstName = "John";

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                null,
                null,
                null,
                null,
                firstName,
                null
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesByLastName() {
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), new ArrayList<>());

        String lastName = "Doe";

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                null,
                null,
                null,
                null,
                null,
                lastName
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesByProjectDates() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10));
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));
        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 19);
        LocalDate endDate = LocalDate.of(2025, 2, 20);

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                null,
                startDate,
                endDate,
                null,
                null,
                null
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesBySkillAndExperience() {
        Skill skill = createAndSaveSkill("Java");
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), List.of(skill), new ArrayList<>());
        skill.setEmployees(employeeRepository.findAll());
        skillRepository.save(skill);

        Integer experience = 3;

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                new String[]{getSkillIdByLabel("Java")},
                null,
                null,
                experience,
                null,
                null
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindFilteredEmployeesByFirstNameAndLastName() {
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), new ArrayList<>());

        String firstName = "John";
        String lastName = "Doe";

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                null,
                null,
                null,
                null,
                firstName,
                lastName
        );

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("John");
        assertThat(filteredEmployees.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    void testFindAllWithoutFilters() {
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), new ArrayList<>());
        createAndSaveEmployee("Jane", "Doe", LocalDate.of(2018, 6, 22), new ArrayList<>(), new ArrayList<>());
        createAndSaveEmployee("Alice", "Smith", LocalDate.of(2020, 1, 15), new ArrayList<>(), new ArrayList<>());

        List<Employee> employees = employeeRepository.findFilteredEmployees(null, null, null, null, null, null);

        assertThat(employees).hasSize(3);
    }

    @Test
    void testFindFilteredEmployees_NoMatchingResults() throws Exception {
        Skill skill = createAndSaveSkill("Python");
        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), List.of(skill), new ArrayList<>());
        skill.setEmployees(employeeRepository.findAll());
        skillRepository.save(skill);

        String javaSkillId = getSkillIdByLabel("Java");

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                new String[]{javaSkillId},
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                10,
                "Jane",
                "Smith"
        );

        assertThat(filteredEmployees).isEmpty();
    }

    @Test
    void testFindFilteredEmployees_WithMultipleEmployees() {
        Skill javaSkill = createAndSaveSkill("Java");
        Skill pythonSkill = createAndSaveSkill("Python");

        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), List.of(javaSkill), new ArrayList<>());
        createAndSaveEmployee("Jane", "Doe", LocalDate.of(2018, 6, 22), List.of(pythonSkill), new ArrayList<>());
        createAndSaveEmployee("Alice", "Smith", LocalDate.of(2020, 1, 15), List.of(javaSkill), new ArrayList<>());

        List<Employee> employees = new ArrayList<>();
        employees.add(employeeRepository.findByFirstname("John").get(0));
        employees.add(employeeRepository.findByFirstname("Alice").get(0));
        javaSkill.setEmployees(employees);
        pythonSkill.setEmployees(employeeRepository.findByFirstname("Jane"));
        skillRepository.save(javaSkill);
        skillRepository.save(pythonSkill);

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                new String[]{getSkillIdByLabel("Java")},
                null,
                null,
                null,
                null,
                null
        );

        assertThat(filteredEmployees).hasSize(2);
        assertThat(filteredEmployees).extracting(Employee::getFirstname).containsExactlyInAnyOrder("John", "Alice");
    }

    @Test
    void testFindFilteredEmployees_BySkillAndExperience() {
        Skill javaSkill = createAndSaveSkill("Java");

        createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), List.of(javaSkill), new ArrayList<>());
        createAndSaveEmployee("Jane", "Doe", LocalDate.of(2015, 6, 22), List.of(javaSkill), new ArrayList<>());

        javaSkill.setEmployees(employeeRepository.findAll());
        skillRepository.save(javaSkill);

        Integer experience = 6;

        List<Employee> filteredEmployees = employeeRepository.findFilteredEmployees(
                new String[]{getSkillIdByLabel("Java")},
                null,
                null,
                experience,
                null,
                null
        );

        assertThat(filteredEmployees).hasSize(1);
        assertThat(filteredEmployees.get(0).getFirstname()).isEqualTo("Jane");
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeAvailable() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 20);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isTrue();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeUnavailable() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 20);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeUnavailableStartEqualEnd() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 20));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 20);
        LocalDate endDate = LocalDate.of(2025, 2, 21);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeUnavailableEndEqualStart() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 20));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 8);
        LocalDate endDate = LocalDate.of(2025, 2, 10);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeUnavailableStartOverlap() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 7), LocalDate.of(2025, 2, 14));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 5);
        LocalDate endDate = LocalDate.of(2025, 2, 10);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeUnavailableEndOverlap() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 7), LocalDate.of(2025, 2, 14));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 10);
        LocalDate endDate = LocalDate.of(2025, 2, 20);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeUnavailableCollapse() {
        Project project = createAndSaveProject("Test Project", LocalDate.of(2025, 2, 7), LocalDate.of(2025, 2, 14));
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project));

        project.setEmployees(employeeRepository.findAll());
        projectRepository.save(project);

        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 2, 28);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_EmployeeWithoutProjects() {
        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), new ArrayList<>());

        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 20);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isTrue();
    }

    @Test
    void testCheckEmployeeAvailability_MultipleProjects() {
        Project project1 = createAndSaveProject("Project 1", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10));
        Project project2 = createAndSaveProject("Project 2", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 20));

        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project1, project2));
        project1.setEmployees(employeeRepository.findAll());
        projectRepository.save(project1);
        project2.setEmployees(employeeRepository.findAll());
        projectRepository.save(project2);
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 16);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }

    @Test
    void testCheckEmployeeAvailability_MultipleProjects_NoConflict() {
        Project project1 = createAndSaveProject("Project 1", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10));
        Project project2 = createAndSaveProject("Project 2", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 20));

        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project1, project2));

        LocalDate startDate = LocalDate.of(2025, 2, 21);
        LocalDate endDate = LocalDate.of(2025, 2, 22);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isTrue();
    }

    @Test
    void testCheckEmployeeAvailability_MultipleProjects_Overlap() {
        Project project1 = createAndSaveProject("Project 1", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10));
        Project project2 = createAndSaveProject("Project 2", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 20));

        Employee employee = createAndSaveEmployee("John", "Doe", LocalDate.of(2019, 11, 12), new ArrayList<>(), List.of(project1, project2));

        project1.setEmployees(employeeRepository.findAll());
        projectRepository.save(project1);
        project2.setEmployees(employeeRepository.findAll());
        projectRepository.save(project2);

        LocalDate startDate = LocalDate.of(2025, 2, 7);
        LocalDate endDate = LocalDate.of(2025, 2, 14);

        boolean isAvailable = employeeRepository.isEmployeeAvailable(employee.getId(), startDate, endDate);

        assertThat(isAvailable).isFalse();
    }




}

