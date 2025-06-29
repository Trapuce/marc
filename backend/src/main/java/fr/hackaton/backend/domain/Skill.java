package fr.hackaton.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skills")
@Builder
public class Skill implements Serializable {

    
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String label;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Employee> employees = new ArrayList<>();
}
