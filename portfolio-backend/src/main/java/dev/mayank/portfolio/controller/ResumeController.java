package dev.mayank.portfolio.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private static final String RESUME_CLASSPATH = "static/resume.pdf";
    private static final String RESUME_FILENAME = "MayankGupta_Resume.pdf";

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResume() {

        try {
            Resource resource = new ClassPathResource(RESUME_CLASSPATH);

            if (!resource.exists() || !resource.isReadable()) {
                log.warn("Resume not found at classpath:{}", RESUME_CLASSPATH);
                return ResponseEntity.notFound().build();
            }

            log.info("Resume download requested — serving {}", RESUME_FILENAME);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + RESUME_FILENAME + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error serving resume", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
