package com.starter.springboot.services;

import com.starter.springboot.domain.User;
import com.starter.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Autowired
    private UserRepository userRepository;

    /**
     * Method for getting all users
     *
     * @return List of user objects.
     */
    
    public User addUser(User user) {
    	user.setPassword(getPasswordEncoder().encode(user.getPassword()));
    	return userRepository.save(user);
    }
    
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Method for getting e-mail by username (key)
     *
     * @param username - provided username
     * @return e-mail
     */
    public String findEmailByUsername(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get().getEmail();
        }
        return null;
    }
}
