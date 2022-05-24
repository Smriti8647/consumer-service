package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.ForgotPasswordRequest;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.Tag;
import com.tweetapp.model.UpdatePasswordRequest;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.TagRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TagRepository tagRepository;

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

//	public UserResponse getUser(String loginId) {
//		Optional<User> user = userRepository.findById(loginId);
//		if (!user.isPresent()) {
//			throw new ResourceNotFoundException("User not present in Database");
//		}
//		return populateUserResponse(user.get());
//	}

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
	
	public boolean forgotPassword(ForgotPasswordRequest request, String loginId) {
		Optional<User> user =  userRepository.findById(loginId);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not present in Database");
		}
		if(user.get().getQuestion()==request.getQues() && user.get().getAns()==request.getAns()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		Optional<User> user =  userRepository.findById(updatePasswordRequest.getLoginId());
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not present in Database");
		}
		user.get().setPassword(updatePasswordRequest.getNewPassword());
		userRepository.save(user.get());
	}

	public List<UserResponse> searchUsers(String loginId) {
		List<User> userList =  userRepository.findUsersByPartialId(loginId);
		//TODO- to decide whether we want to throw an exception or send an empty list,
		// current implementation is sending empty list
//		if(userList.isEmpty()) {
//			throw new ResourceNotFoundException("User not present in Database");
//		}
		List<UserResponse> userResponseList=new ArrayList<>();
		userList.forEach(user->{
			UserResponse userResposne=new UserResponse();
			userResposne=populateUserResponse(user);
			userResponseList.add(userResposne);
		});
		return userResponseList;
	}
	
	public Tag taggedTweets(String loginId) {
		Optional<Tag> tag=tagRepository.findById(loginId);
		return tag.get();
	}
	
	public void tagUser(String loginId, String tweetId) {
		Optional<Tag> tag=tagRepository.findById(loginId);
		if(tag.isEmpty()) {
			Tag tagRecord=new Tag();
			tagRecord.setLoginId(loginId);
			List<String> tweetIdList=new ArrayList<>();
			tweetIdList.add(tweetId);
			tagRecord.setTweetIdList(tweetIdList);
			tagRepository.insert(tagRecord);
		}
		else {
			List<String> tweetIdList=tag.get().getTweetIdList();
			tweetIdList.add(tweetId);
			tag.get().setTweetIdList(tweetIdList);
			tagRepository.save(tag.get());
		}
	}
}
