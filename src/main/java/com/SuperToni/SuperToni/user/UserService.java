package com.SuperToni.SuperToni.user;

import java.lang.module.ResolutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(int id){
        User user = userRepository.getUserById(id);
        if(user == null){
            throw new ResolutionException("The user with id " + id + "was not found");
        }
        return user;
    }

    @Transactional
    public User getUserByName(String name){
        User user = userRepository.getUserByName(name);
        return user;
    }

    public Boolean existsUser(String userName) {
		return userRepository.existsByUserName(userName);
	}
}
