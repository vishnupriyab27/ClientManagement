package com.assignment.service;

import com.assignment.dto.ClientRequest;
import com.assignment.dto.ClientResponse;
import com.assignment.dto.PageResponse;
import com.assignment.entity.Client;
import com.assignment.mapper.ClientMapper;
import com.assignment.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ClientService {
    private final ClientRepository repo;
    public ClientService(ClientRepository repo) { this.repo = repo; }

    public ClientResponse create(ClientRequest req) throws Exception {
        if (repo.existsByEmail(req.email())) {
            throw new Exception("email already exists");
        }
        Client c = ClientMapper.toEntity(req);
        return ClientMapper.toResponse(repo.save(c));
    }

    @Transactional(readOnly = true)
    public ClientResponse get(UUID id) throws Exception {
        Client c = repo.findById(id).orElseThrow(() -> new Exception("client not found"));
        return ClientMapper.toResponse(c);
    }

    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> list(int page, int size, String sort) {
        Sort sortObj = sort == null ? Sort.by("fullName","displayName").ascending() : Sort.by(sort.split(","));
        Page<Client> p = repo.findAll(PageRequest.of(page, size, sortObj));
        return new PageResponse<>(
                p.map(ClientMapper::toResponse).getContent(),
                p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(),
                p.isFirst(), p.isLast()
        );
    }

    public ClientResponse update(UUID id, ClientRequest req) throws Exception {
        Client c = repo.findById(id).orElseThrow(() -> new Exception("client not found"));
        if (!c.getEmail().equals(req.email()) && repo.existsByEmail(req.email())) {
            throw new Exception("email already exists");
        }
        ClientMapper.updateEntity(c, req);
        return ClientMapper.toResponse(c);
    }

    public ClientResponse patch(UUID id, ClientRequest req) throws Exception { return update(id, req); }

    public void delete(UUID id) throws Exception {
        if (!repo.existsById(id)) throw new Exception("client not found");
        repo.deleteById(id);
    }


}
