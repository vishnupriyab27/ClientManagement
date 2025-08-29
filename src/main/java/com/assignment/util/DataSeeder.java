package com.assignment.util;

import com.assignment.dto.ClientsWrapper;
import com.assignment.entity.Client;
import com.assignment.repository.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ClientRepository repo;
    private final ResourceJsonReader resourceJsonReader;
    public DataSeeder(ClientRepository repo, ResourceJsonReader resourceJsonReader) {
        this.repo = repo;
        this.resourceJsonReader = resourceJsonReader;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;
        ClientsWrapper wrapper = this.resourceJsonReader.readJsonFile("clients.json", new TypeReference<ClientsWrapper>() {});
        List<Client> clients = wrapper.getClients();
        repo.saveAll(clients);
    }
}
