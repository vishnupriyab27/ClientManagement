package com.assignment.dto;

import com.assignment.entity.Client;

import java.util.List;

public class ClientsWrapper {
    private List<Client> clients;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
