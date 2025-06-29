package fr.hackaton.backend.domain;

import fr.hackaton.backend.repository.ProjectRepository;
import fr.hackaton.backend.repository.SkillRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
@Builder
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "TEXT" , nullable = true) 
    private String urlCV;

    @Column(nullable = true)
    private LocalDate careerStart;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_skills",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @ToString.Exclude
    @Builder.Default
    private List<Skill> skills =new ArrayList<>();

    public void addSKill(Skill skill) {
        skills.add(skill);
        skill.getEmployees().add(this) ;
    }

    public void removeSkill(Skill skill){
        skills.remove(skill);
        skill.getEmployees().remove(this) ;

    }

    @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private List<Project> projects = Collections.emptyList();

    public void removeProject(Project project){
        projects.remove(project);
        project.getEmployees().remove(this) ;

    }
}
