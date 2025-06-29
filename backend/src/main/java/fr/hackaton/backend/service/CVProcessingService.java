package fr.hackaton.backend.service;

import fr.hackaton.backend.domain.Skill;
import fr.hackaton.backend.dto.skills.SkillDTO;
import fr.hackaton.backend.repository.SkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class CVProcessingService {

    private final SkillRepository skillRepository;

    public CVProcessingService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<SkillDTO> extractSkillsFromCV(MultipartFile cvFile) {
        String cvContent = extractTextFromCV(cvFile);
        return matchSkills(cvContent);
    }

    private String extractTextFromCV(MultipartFile cvFile) {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(cvFile.getInputStream()))) {
            PDFTextStripper stripper = new PDFTextStripper();
            System.out.println(stripper.getText(document).toLowerCase());
            return stripper.getText(document).toLowerCase(); 
        } catch (IOException e) {
            log.error("Erreur lors de l'extraction du texte du CV", e);
            throw new IllegalArgumentException("Impossible d'extraire le texte du CV", e);
        }
    }

    private List<SkillDTO> matchSkills(String cvContent) {
        List<Skill> availableSkills = skillRepository.findAll(); 
        List<SkillDTO> matchedSkills = new ArrayList<>();

        for (Skill skill : availableSkills) {
            if (cvContent.contains(skill.getLabel().toLowerCase())) {
                matchedSkills.add(new SkillDTO(skill.getId(), skill.getLabel()));
            }
        }

        return matchedSkills;
    }
}
