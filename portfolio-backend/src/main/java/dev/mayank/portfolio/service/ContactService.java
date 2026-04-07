package dev.mayank.portfolio.service;

import dev.mayank.portfolio.model.ContactRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    @Value("${portfolio.email.to:ce.mayank8@gmail.com}")
    private String toEmail;

    @Value("${portfolio.email.from:noreply@mayank.dev}")
    private String fromEmail;

    /**
     * Sends a contact email notification to Mayank and a confirmation to the sender.
     *
     * @param request the validated contact form payload
     */
    public void sendContactEmail(ContactRequest request) {
        log.info("Processing contact form submission from: {}", request.getEmail());

        sendNotificationToMayank(request);
        sendConfirmationToSender(request);

        log.info("Contact emails sent successfully for: {}", request.getEmail());
    }

    private void sendNotificationToMayank(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setReplyTo(request.getEmail());
        message.setSubject("[Portfolio Contact] " +
                (request.getSubject() != null && !request.getSubject().isBlank()
                        ? request.getSubject()
                        : "New message from " + request.getName()));

        String body = """
                New contact form submission from your portfolio!
                
                ──────────────────────────────────────
                Name    : %s
                Email   : %s
                Subject : %s
                ──────────────────────────────────────
                
                Message:
                %s
                
                ──────────────────────────────────────
                Reply directly to this email to respond.
                """.formatted(
                request.getName(),
                request.getEmail(),
                request.getSubject() != null ? request.getSubject() : "N/A",
                request.getMessage()
        );

        message.setText(body);
        mailSender.send(message);
    }

    private void sendConfirmationToSender(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setFrom(fromEmail);
        message.setSubject("Thanks for reaching out, " + request.getName() + "!");

        String body = """
                Hi %s,
                
                Thanks for reaching out! I've received your message and will get back to you within 24 hours.
                
                ──────────────────────────────────────
                Your message:
                %s
                ──────────────────────────────────────
                
                In the meantime, feel free to connect with me:
                • GitHub  : https://github.com/mayankSystems
                • LinkedIn: https://linkedin.com/in/mayanksystems
                
                Best regards,
                Mayank Gupta
                Senior Backend Engineer | PayU Digital Labs
                ce.mayank8@gmail.com
                """.formatted(request.getName(), request.getMessage());

        message.setText(body);
        mailSender.send(message);
    }
}
