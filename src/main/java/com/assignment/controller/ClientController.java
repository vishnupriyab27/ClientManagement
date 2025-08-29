package com.assignment.controller;

import com.assignment.dto.ClientRequest;
import com.assignment.dto.ClientResponse;
import com.assignment.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Clients", description = "Client Management API")
public class ClientController {
    private final ClientService service;
    public ClientController(ClientService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a client")
    public ClientResponse create(@Valid @RequestBody ClientRequest req) { return service.create(req); }

    @GetMapping("/{id}")
    @Operation(summary = "Get a client by id")
    public ClientResponse get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    @Operation(summary = "Get clients (by name or id) with Sorting")
    public List<ClientResponse> getClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String direction
    ) {
        return service.getClients(name, id, sortBy, direction);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all clients")
    public List<ClientResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client (full)")
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest req) { return service.update(id, req); }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a client (partial)")
    public ClientResponse patch(@PathVariable Long id, @RequestBody ClientRequest req) { return service.patch(id, req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a client")
    public void delete(@PathVariable Long id) { service.delete(id); }

}
