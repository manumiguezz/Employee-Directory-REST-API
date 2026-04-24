package com.manumiguezz.crudapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
        @NotBlank(message = "firstName is required")
        @Size(max = 50, message = "firstName must not exceed 50 characters")
        String firstName,
        @NotBlank(message = "lastName is required")
        @Size(max = 50, message = "lastName must not exceed 50 characters")
        String lastName,
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        @Size(max = 100, message = "email must not exceed 100 characters")
        String email
) {
}
