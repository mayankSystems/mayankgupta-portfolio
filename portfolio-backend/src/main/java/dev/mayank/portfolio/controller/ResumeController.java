package dev.mayank.portfolio.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Value("${portfolio.resume.path:classpath:static/resume.pdf}")
    private String resumePath;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResume() {
        try {
            Path path = Paths.get(resumePath.replace("classpath:", "src/main/resources/static/"));
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.warn("Resume file not found or not readable at path: {}", resumePath);
                return ResponseEntity.notFound().build();
            }

            log.info("Resume download requested");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"MayankGupta_Resume.pdf\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            log.error("Invalid resume path: {}", resumePath, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
