package com.SuperToni.SuperToni.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SuperToni.SuperToni.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }
    
    @Transactional
    public Client saveClient(Client client) throws DataAccessException{
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClientByID(int id) throws DataAccessException{
        Client client = clientRepository.getClientById(id).get();
        if (client.getId() == null) {
            throw new ResourceNotFoundException("Client id was not found" + id);

        }
        clientRepository.delete(client);
    }

    @Transactional
    public Client getClientByEmail(String email) throws DataAccessException{
        Client client = clientRepository.getClientByEmail(email).get();
        return client;
    
    }

}
