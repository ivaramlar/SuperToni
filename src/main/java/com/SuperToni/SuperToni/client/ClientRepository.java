package com.SuperToni.SuperToni.client;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("SELECT Distinct c from Client c where c.email = :email")
    public Optional<Client> getClientByEmail(String email);

    @Query("SELECT Distinct c from Client c where c.id = :id")
    public Optional<Client> getClientById(int id);
    
    
}
