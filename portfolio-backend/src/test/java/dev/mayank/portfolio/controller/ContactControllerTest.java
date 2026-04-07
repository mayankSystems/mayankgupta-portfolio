package dev.mayank.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mayank.portfolio.model.ContactRequest;
import dev.mayank.portfolio.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactService contactService;

    @Test
    void sendMessage_validRequest_returns200() throws Exception {
        ContactRequest request = new ContactRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setSubject("Job Opportunity");
        request.setMessage("Hi Mayank, I have an exciting role for you!");

        doNothing().when(contactService).sendContactEmail(any());

        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void sendMessage_missingName_returns400() throws Exception {
        ContactRequest request = new ContactRequest();
        request.setEmail("john@example.com");
        request.setMessage("A valid message here that is long enough.");

        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void sendMessage_invalidEmail_returns400() throws Exception {
        ContactRequest request = new ContactRequest();
        request.setName("John Doe");
        request.setEmail("not-an-email");
        request.setMessage("A valid message here that is long enough.");

        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendMessage_serviceThrows_returns500() throws Exception {
        ContactRequest request = new ContactRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setMessage("A valid message here that is long enough.");

        doThrow(new RuntimeException("SMTP error")).when(contactService).sendContactEmail(any());

        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false));
    }
}
