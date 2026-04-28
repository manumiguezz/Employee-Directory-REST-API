package com.manumiguezz.employeedirectory.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EmployeeRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmployeesForEmployeeRole() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .with(httpBasic("ramiro", "examplepass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("manum@mail.com"));
    }

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnNotFoundForMissingEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/999")
                        .with(httpBasic("ramiro", "examplepass")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").value("Employee id not found - 999"));
    }

    @Test
    void shouldCreateEmployeeForManagerRole() throws Exception {
        String requestBody = """
                {
                  "firstName": "Ana",
                  "lastName": "Lopez",
                  "email": "ana.lopez@mail.com"
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .with(httpBasic("matias", "examplepass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/employees/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.email").value("ana.lopez@mail.com"));
    }

    @Test
    void shouldUpdateEmployeeForManagerRole() throws Exception {
        String requestBody = """
                {
                  "firstName": "Manuel",
                  "lastName": "Miguez",
                  "email": "manuel.updated@mail.com"
                }
                """;

        mockMvc.perform(put("/api/employees/1")
                        .with(httpBasic("matias", "examplepass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("manuel.updated@mail.com"));
    }

    @Test
    void shouldRejectUpdateWhenEmailAlreadyExists() throws Exception {
        String requestBody = """
                {
                  "firstName": "Manuel",
                  "lastName": "Miguez",
                  "email": "lucav@mail.com"
                }
                """;

        mockMvc.perform(put("/api/employees/1")
                        .with(httpBasic("matias", "examplepass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.detail").value("An employee with that email already exists."));
    }

    @Test
    void shouldRejectCreateWhenPayloadIsInvalid() throws Exception {
        String requestBody = """
                {
                  "firstName": "",
                  "lastName": "Lopez",
                  "email": "invalid-email"
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .with(httpBasic("matias", "examplepass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.firstName").value("firstName is required"))
                .andExpect(jsonPath("$.errors.email").value("email must be a valid email address"));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        String requestBody = """
                {
                  "firstName": "Ana",
                  "lastName": "Lopez",
                  "email": "ana.lopez@mail.com"
                """;

        mockMvc.perform(post("/api/employees")
                        .with(httpBasic("matias", "examplepass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Malformed JSON request"));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        String requestBody = """
                {
                  "firstName": "Ana",
                  "lastName": "Lopez",
                  "email": "manum@mail.com"
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .with(httpBasic("matias", "examplepass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.detail").value("An employee with that email already exists."));
    }

    @Test
    void shouldRejectDeleteForNonAdminUsers() throws Exception {
        mockMvc.perform(delete("/api/employees/1")
                        .with(httpBasic("matias", "examplepass")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteEmployeeForAdminRole() throws Exception {
        mockMvc.perform(delete("/api/employees/1")
                        .with(httpBasic("alejo", "examplepass")))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/employees/1")
                        .with(httpBasic("ramiro", "examplepass")))
                .andExpect(status().isNotFound());
    }
}
