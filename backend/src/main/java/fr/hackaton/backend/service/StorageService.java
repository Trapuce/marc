package fr.hackaton.backend.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {
    
    @Value("${minio.bucket.name}")
    private String bucketName;
    
    
    private final  MinioClient minioClient;


    public StorageService(MinioClient minioClient){
        this.minioClient = minioClient ;
    }
    
    
    public String uploadCV(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            String contentType = file.getContentType();
            if (contentType == null || !isValidFileType(contentType)) {
                throw new IllegalArgumentException("Invalid file type. Only PDF and DOC/DOCX files are allowed.");
            }
            
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object("cv/" + filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build()
            );
            
            String fileUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object("cv/" + filename)
                    .expiry(7, TimeUnit.DAYS)  
                    .build()
            );
            
            log.info("File uploaded successfully: {}", filename);
            return fileUrl;
            
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to upload CV file", e);
        }
    }
    
    private boolean isValidFileType(String contentType) {
        return contentType.equals("application/pdf") ||
               contentType.equals("application/msword") ||
               contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }
    
    public void deleteCV(String filename) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object("cv/" + filename)
                    .build()
            );
            log.info("File deleted successfully: {}", filename);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to delete CV file", e);
        }
    }



    public String generatePresignedUrl(String objectPath, int expirationMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectPath)
                    .expiry(expirationMinutes, TimeUnit.MINUTES)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to generate download URL", e);
        }
    }

        /**
     * Extrait le nom du fichier original à partir du chemin stocké
     * @param objectPath le chemin complet du fichier (ex: "cv/uuid_nomfichier.pdf")
     * @return le nom original du fichier
     */
    public String extractFilename(String objectPath) {
        if (objectPath == null || objectPath.isEmpty()) {
            return null;
        }

        if (objectPath.contains("/")) {
            objectPath = objectPath.substring(objectPath.lastIndexOf("/") + 1);
        }

        if (objectPath.contains("_")) {
            return objectPath.substring(objectPath.indexOf("_") + 1);
        }

        return objectPath;
    }

}

