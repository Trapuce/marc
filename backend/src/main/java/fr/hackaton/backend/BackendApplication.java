package fr.hackaton.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.repository.SkillRepository;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private SkillRepository skillRepository;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> skills = List.of(
                "RabbitMQ", "Docker", "Selenium", "MySQL", "SQLite",
                "Maria DB", "MongoDB", "neo4j", "DuckDB", "Postgresql",
                "Java", "JavaScript", "TypeScript", "C", "Python",
                "PHP", "C++", "C#", "React", "Angular");

        // Ajouter les compétences dans la base de données
        addSkills(skills);

    }

    private void addSkills(List<String> skillNames) {
        for (String skillName : skillNames) {
            String skillLabel = skillName.toLowerCase();
            Skill skill = new Skill();
            skill.setId(skillLabel);
            skill.setLabel(skillName);
            skillRepository.save(skill);

        }

    }

}
