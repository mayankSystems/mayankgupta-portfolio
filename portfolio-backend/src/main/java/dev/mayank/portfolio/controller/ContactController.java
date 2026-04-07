package dev.mayank.portfolio.controller;

import dev.mayank.portfolio.model.ApiResponse;
import dev.mayank.portfolio.model.ContactRequest;
import dev.mayank.portfolio.service.ContactService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // Simple in-memory rate limiter: 3 requests per IP per hour
    private final Map<String, Bucket> rateLimiters = new ConcurrentHashMap<>();

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendMessage(
            @Valid @RequestBody ContactRequest request,
            HttpServletRequest httpRequest) {

        String clientIp = getClientIp(httpRequest);
        Bucket bucket = rateLimiters.computeIfAbsent(clientIp, this::createBucket);

        if (!bucket.tryConsume(1)) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error("Too many requests. Please wait before sending another message."));
        }

        try {
            contactService.sendContactEmail(request);
            return ResponseEntity.ok(ApiResponse.success(
                    "Message sent successfully! I'll get back to you within 24 hours."));
        } catch (Exception e) {
            log.error("Failed to send contact email from: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send message. Please try emailing me directly at ce.mayank8@gmail.com"));
        }
    }

    private Bucket createBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
