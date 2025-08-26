package com.assignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Size(max = 100) String displayName,
        @NotBlank @Email @Size(max = 100) String email,
        @Size(max = 500) String details,
        @Size(max = 255) String location,
        Boolean status
) {}
