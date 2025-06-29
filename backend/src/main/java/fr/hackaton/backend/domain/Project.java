package fr.hackaton.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
@Builder
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    private String color;

    @Column(nullable = false)
    private String managerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String client;

    @Column(nullable = true)
    private LocalDate archivedAt;

    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_employees",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @ToString.Exclude
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.employees.add(employee);
        employee.getProjects().add(this);
    }

    public void removeEmployee(Employee employee) {
        if (this.employees != null) {
            this.employees.remove(employee);
            employee.getProjects().remove(this);
        }
    }

    public void clearEmployees() {
        if (this.employees != null) {
            this.employees.forEach(employee -> employee.getProjects().remove(this));
            this.employees.clear();
        }
    }
}
