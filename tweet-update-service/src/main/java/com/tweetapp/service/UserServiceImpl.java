package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	public String saveUser(User user) {
		Boolean isUserWithEmailPresent = userRepository.findUserByEmail(user.getEmail()).isPresent();
		Boolean isUserWithIdPresent = userRepository.findById(user.getLoginId()).isPresent();
		if (isUserWithIdPresent) {
			return "this login id is already registered";
		} else if (isUserWithEmailPresent) {
			return "this email id is already registered";
		} else {
			user.setRole("ROLE_USER");
			userRepository.save(user);
			return "successful with id " + user.getLoginId();
		}
	}

	public UserResponse getUser(String loginId) {
		Optional<User> user = userRepository.findById(loginId);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not present in Database");
		}
		return populateUserResponse(user.get());
	}

	private UserResponse populateUserResponse(User user) {
		UserResponse userResponse=new UserResponse();
		userResponse.setAvtar(user.getAvtar());
		userResponse.setLoginId(user.getLoginId());
		userResponse.setName(user.getFirstName());
		return userResponse;
	}

	public List<UserResponse> getAllUsers() {
		List<User> userList = userRepository.findAll();
		if (userList.isEmpty()) {
			throw new ResourceNotFoundException("No user Present in Database");
		}		
		List<UserResponse> userResponseList=new ArrayList<>();
		userList.forEach(user->{
			UserResponse userResposne=new UserResponse();
			userResposne=populateUserResponse(user);
			userResponseList.add(userResposne);
		});
		//userResposne=populateUserResponse()
		return userResponseList;
	}
	
	public LoginResponse login(String loginId) {
		LoginResponse loginResponse= new LoginResponse();
		Optional<User> user = userRepository.findById(loginId);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not present in Database");
		}
		loginResponse.setLoginId(user.get().getLoginId());
		loginResponse.setPassword(user.get().getPassword());
		loginResponse.setRole(user.get().getRole());
		return loginResponse;
	}
	

}
