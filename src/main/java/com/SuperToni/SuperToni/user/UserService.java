package com.SuperToni.SuperToni.user;

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

    public Boolean existsUser(String userName) {
		return userRepository.existsByUserName(userName);
	}
}
