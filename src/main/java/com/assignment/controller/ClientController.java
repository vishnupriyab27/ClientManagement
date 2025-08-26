package com.assignment.controller;

import com.assignment.dto.ClientRequest;
import com.assignment.dto.ClientResponse;
import com.assignment.dto.PageResponse;
import com.assignment.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Clients", description = "Client Management API")
public class ClientController {
    private final ClientService service;
    public ClientController(ClientService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a client")
    public ClientResponse create(@Valid @RequestBody ClientRequest req) throws Exception { return service.create(req); }

    @GetMapping("/{id}")
    @Operation(summary = "Get a client by id")
    public ClientResponse get(@PathVariable UUID id) throws Exception { return service.get(id); }

    @GetMapping
    @Operation(summary = "List clients (paged)")
    public PageResponse<ClientResponse> list(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) String sort) {
        return service.list(page, size, sort);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client (full)")
    public ClientResponse update(@PathVariable UUID id, @Valid @RequestBody ClientRequest req) throws Exception { return service.update(id, req); }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a client (partial)")
    public ClientResponse patch(@PathVariable UUID id, @RequestBody ClientRequest req) throws Exception { return service.patch(id, req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a client")
    public void delete(@PathVariable UUID id) throws Exception { service.delete(id); }

}
