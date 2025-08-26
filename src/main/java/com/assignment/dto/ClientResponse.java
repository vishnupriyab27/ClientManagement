package com.assignment.dto;

import java.time.Instant;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        Boolean status,
        Instant createdAt,
        Instant updatedAt
) {
}
