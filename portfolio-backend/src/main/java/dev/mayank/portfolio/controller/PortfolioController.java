package dev.mayank.portfolio.controller;

import dev.mayank.portfolio.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PortfolioController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        return ResponseEntity.ok(ApiResponse.success("Portfolio API is running",
                Map.of(
                        "status", "UP",
                        "owner", "Mayank Gupta",
                        "role", "Senior Backend Engineer"
                )));
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> info() {
        Map<String, Object> info = Map.of(
                "name", "Mayank Gupta",
                "role", "Senior Backend Engineer",
                "company", "PayU Digital Labs Pvt. Ltd.",
                "email", "ce.mayank8@gmail.com",
                "github", "https://github.com/mayankSystems",
                "linkedin", "https://linkedin.com/in/mayanksystems",
                "location", "Bengaluru, Karnataka, India"
        );
        return ResponseEntity.ok(ApiResponse.success("Portfolio info", info));
    }
}
