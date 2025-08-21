package com.SuperToni.SuperToni.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    @Query("SELECT u FROM User u ")
    public List<User> getUsers();
    
    @Query("SELECT DISTINCT u FROM User u WHERE u.id = :id")
    public User getUserById(Integer id);

    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);

}
