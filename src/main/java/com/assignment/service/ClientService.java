package com.assignment.service;

import com.assignment.dto.ClientRequest;
import com.assignment.dto.ClientResponse;
import com.assignment.entity.Client;
import com.assignment.exception.ConflictException;
import com.assignment.exception.NotFoundException;
import com.assignment.mapper.ClientMapper;
import com.assignment.repository.ClientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClientService {
    private final ClientRepository repo;
    public ClientService(ClientRepository repo) { this.repo = repo; }

    public ClientResponse create(ClientRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new ConflictException("email already exists");
        }
        Client c = ClientMapper.toEntity(req);
        return ClientMapper.toResponse(repo.save(c));
    }

    @Transactional(readOnly = true)
    public ClientResponse get(Long id) {
        Client c = repo.findById(id).orElseThrow(() -> new NotFoundException("client not found"));
        return ClientMapper.toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> getAll() {
        List<Client> clients = repo.findAll();
        return buildClientResponse(clients);
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> getClients(String name, Long id, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction),
                    sortBy != null ? sortBy : "id"
                );

        List<Client> clients;

        if(id != null){
            clients = repo.findById(id, sort);
        } else if(name != null){
            clients = repo.findByFullNameContaining(name, sort);
        } else{
            clients = repo.findAll(sort);
        }

        return buildClientResponse(clients);
    }

    public ClientResponse update(Long id, ClientRequest req) {
        Client c = repo.findById(id).orElseThrow(() -> new NotFoundException("client not found"));
        if (!c.getEmail().equals(req.email()) && repo.existsByEmail(req.email())) {
            throw new ConflictException("email already exists");
        }
        ClientMapper.updateEntity(c, req);
        return ClientMapper.toResponse(repo.save(c));
    }

    public ClientResponse patch(Long id, ClientRequest req) { return update(id, req); }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("client not found");
        repo.deleteById(id);
    }

    private List<ClientResponse> buildClientResponse(List<Client> clients) {
        List<ClientResponse> res = new ArrayList<>();
        if(clients != null && !clients.isEmpty()){
            for (Client client : clients) {
                res.add(ClientMapper.toResponse(client));
            }
        }
        return res;
    }

}
