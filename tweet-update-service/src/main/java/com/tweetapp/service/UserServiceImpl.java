package com.tweetapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public String saveUser(User user) {
		Boolean isUserPresent=userRepository.findUserByEmail(user.getEmail()).isPresent();
		if(isUserPresent) {
			//need to throw exception here
			return "this email id is already registered"; 
		}
		else {
			userRepository.save(user);
			return "successful with id "+user.getLoginId();
		}		
	}
	
	public User getUser(String loginId) {
		Optional<User> user = userRepository.findById(loginId);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not present in Database");		
		}
		return user.get();
	}
	
	public List<User> getAllUsers() {
		List<User> userList = userRepository.findAll();
		if(userList.isEmpty()) {
			throw new ResourceNotFoundException("No user Present in Database");
		}
		return userList;
	}

}
