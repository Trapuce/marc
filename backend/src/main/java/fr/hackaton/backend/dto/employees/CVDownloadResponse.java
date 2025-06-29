package fr.hackaton.backend.dto.employees;

import lombok.Data;

@Data
public class CVDownloadResponse {
    private String downloadUrl;
    private Long expiresIn;  
    private String filename;
}
