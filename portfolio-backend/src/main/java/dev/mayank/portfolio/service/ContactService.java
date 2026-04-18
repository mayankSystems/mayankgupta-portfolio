package dev.mayank.portfolio.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import dev.mayank.portfolio.model.ContactRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    @Value("${portfolio.email.to}")
    private String toEmail;

    @Value("${portfolio.email.from}")
    private String fromEmail;

    public void sendContactEmail(ContactRequest request) {
        log.info("Processing contact form submission from: {}", request.getEmail());

        sendNotificationToMayank(request);
        sendConfirmationToSender(request);

        log.info("Contact emails sent successfully for: {}", request.getEmail());
    }

    private void sendNotificationToMayank(ContactRequest request) {
        try {
            Email from = new Email(fromEmail);
            Email to = new Email(toEmail);

            String subject = "[Portfolio Contact] " +
                    (request.getSubject() != null && !request.getSubject().isBlank()
                            ? request.getSubject()
                            : "New message from " + request.getName());

            String html = """
                    <html>
                    <body style="font-family:Arial;background:#0a0e14;color:#cdd6e0;padding:20px;">
                        <div style="max-width:600px;margin:auto;background:#111720;padding:20px;border-radius:10px;border:1px solid #1e2d3d;">
                            <h2 style="color:#00d8ff;">📩 New Portfolio Message</h2>

                            <p><strong>Name:</strong> %s</p>
                            <p><strong>Email:</strong> %s</p>
                            <p><strong>Subject:</strong> %s</p>

                            <hr style="border:1px solid #243447;"/>

                            <p><strong>Message:</strong><br/>%s</p>

                            <hr style="border:1px solid #243447;"/>

                            <p style="font-size:12px;color:#8899aa;">
                                Source: Portfolio Contact Form<br/>
                                Timestamp: %s
                            </p>
                        </div>
                    </body>
                    </html>
                    """.formatted(
                    request.getName(),
                    request.getEmail(),
                    request.getSubject() != null ? request.getSubject() : "N/A",
                    request.getMessage(),
                    java.time.LocalDateTime.now()
            );

            Content content = new Content("text/html", html);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
            Request req = new Request();

            req.setMethod(Method.POST);
            req.setEndpoint("mail/send");
            req.setBody(mail.build());

            Response response = sg.api(req);

            log.info("SendGrid response: status={}, body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid failed");
            }

        } catch (Exception e) {
            log.error("SendGrid email failed", e);
            throw new RuntimeException("Failed to send email");
        }
    }

    private void sendConfirmationToSender(ContactRequest request) {
        try {
            Email from = new Email(fromEmail);
            Email to = new Email(request.getEmail());

            String subject = "Thanks for reaching out, " + request.getName() + "!";

            String html = """
                    <html>
                    <body style="font-family:Arial;background:#0a0e14;color:#cdd6e0;padding:20px;">
                        <div style="max-width:600px;margin:auto;background:#111720;padding:20px;border-radius:10px;border:1px solid #1e2d3d;">
                            <h2 style="color:#00ff9d;">✅ Message Received</h2>

                            <p>Hi %s,</p>
                            <p>Thanks for reaching out! I'll get back to you within <strong>24 hours</strong>.</p>

                            <hr style="border:1px solid #243447;"/>

                            <p><strong>Your Message:</strong><br/>%s</p>

                            <hr style="border:1px solid #243447;"/>

                            <p style="font-size:12px;color:#8899aa;">
                                Connect with me:<br/>
                                GitHub: github.com/mayankSystems<br/>
                                LinkedIn: linkedin.com/in/mayanksystems
                            </p>
                        </div>
                    </body>
                    </html>
                    """.formatted(
                    request.getName(),
                    request.getMessage()
            );

            Content content = new Content("text/html", html);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
            Request req = new Request();

            req.setMethod(Method.POST);
            req.setEndpoint("mail/send");
            req.setBody(mail.build());

            sg.api(req);

        } catch (Exception e) {
            log.error("Confirmation email failed", e);
        }
    }
}