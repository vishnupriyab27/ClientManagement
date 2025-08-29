package com.assignment.dto;

import java.time.Instant;

public record ClientResponse(
        Long id,
        String fullName,
        String displayName,
        String email,
        String details,
        String location,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
