package com.assignment.mapper;

import com.assignment.dto.ClientRequest;
import com.assignment.dto.ClientResponse;
import com.assignment.entity.Client;

public class ClientMapper {
    public static Client toEntity(ClientRequest r) {
        Client c = new Client();
        c.setFullName(r.fullName());
        c.setDisplayName(r.displayName());
        c.setEmail(r.email());
        c.setDetails(r.details());
        c.setLocation(r.location());
        if (r.active() != null) c.setActive(r.active());
        return c;
    }

    public static void updateEntity(Client c, ClientRequest r) {
        if (r.fullName() != null) c.setFullName(r.fullName());
        if (r.displayName() != null) c.setDisplayName(r.displayName());
        if (r.email() != null) c.setEmail(r.email());
        if (r.details() != null) c.setDetails(r.details());
        if (r.location() != null) c.setLocation(r.location());
        if (r.active() != null) c.setActive(r.active());
    }

    public static ClientResponse toResponse(Client c) {
        return new ClientResponse(
                c.getId(), c.getFullName(), c.getDisplayName(), c.getEmail(),
                c.getDetails(), c.getLocation(), c.getActive(),
                c.getCreatedAt(), c.getUpdatedAt()
        );
    }

}
