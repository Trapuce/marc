package fr.hackaton.backend.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private void executeSQLFile(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            String sql = Files.readString(Paths.get(resource.getURI()));
            entityManager.createNativeQuery(sql).executeUpdate();
            System.out.println("SQL seed executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to execute SQL seed.");
        }
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String sql1 = """
                   CREATE OR REPLACE FUNCTION find_filtered_employees(
                       p_skills_id TEXT[],
                       p_start_date DATE,
                       p_end_date DATE,
                       p_experience INT,
                       p_firstname TEXT,
                       p_lastname TEXT
                   )
                   RETURNS TABLE (
                       id UUID,
                       firstname TEXT,
                       lastname TEXT,
                       email TEXT,
                       urlcv TEXT,
                       career_start DATE,
                       created_at DATE
                   ) AS $$
                   BEGIN
                       RETURN QUERY
                       SELECT DISTINCT e.id,
                                         e.firstname::TEXT,
                                         e.lastname::TEXT,
                                         e.email::TEXT,
                                         e.urlcv::TEXT,
                                         e.career_start,
                                         e.created_at
                       FROM employees e
                       LEFT JOIN employee_skills es ON e.id = es.employee_id
                       LEFT JOIN skills s ON es.skill_id = s.id
                       LEFT JOIN projects p ON e.id = ANY(SELECT employee_id FROM project_employees WHERE project_id = p.id)
                       WHERE
                           (p_skills_id IS NULL OR s.id = ANY(p_skills_id))
                           AND (p_experience IS NULL OR DATE_PART('year', AGE(e.career_start)) >= p_experience)
                           AND (p_firstname IS NULL OR LOWER(e.firstname) = LOWER(p_firstname))
                           AND (p_lastname IS NULL OR LOWER(e.lastname) = LOWER(p_lastname))
                           AND (
                               p_start_date IS NULL OR p_end_date IS NULL OR
                               p.id IS NULL OR NOT\s
                               (
                                   -- Projets consécutifs uniquement, donc on exclut tout chevauchement
                                   (p.start_date < p_end_date AND p.end_date > p_start_date) -- projet se chevauche avec la période donnée
                               )
                           );
                   END;
                   $$ LANGUAGE plpgsql;
                                   
                """;

        String sql2 = """
                    CREATE OR REPLACE FUNCTION is_employee_available(
                                            p_employee_id UUID,
                                            p_start_date DATE,
                                            p_end_date DATE
                                        ) RETURNS BOOLEAN AS $$
                                        BEGIN
                                            RETURN NOT EXISTS (
                                                SELECT 1
                                                FROM project_employees pe
                                                INNER JOIN projects p ON pe.project_id = p.id
                                                WHERE pe.employee_id = p_employee_id
                                                AND (
                                                    -- Projets consécutifs uniquement, donc on exclut tout chevauchement
                                                    (p.start_date <= p_end_date AND p.end_date >= p_start_date) -- projet chevauche la période donnée
                                                )
                                            );
                                        END;
                                        $$ LANGUAGE plpgsql;
                """;
        executeSQLFile("seed_main_test.sql");


        try {
            entityManager.createNativeQuery(sql1).executeUpdate();
            System.out.println("Function 'find_filtered_employees' created successfully.");
            entityManager.createNativeQuery(sql2).executeUpdate();
            System.out.println("Function 'is_employee_available' created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to create function.");
        }
    }

}
