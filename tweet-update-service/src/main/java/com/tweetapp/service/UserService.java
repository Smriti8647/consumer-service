package com.tweetapp.service;

import java.util.List;

import com.tweetapp.model.ForgotPasswordRequest;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.Tag;
import com.tweetapp.model.UpdatePasswordRequest;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;

public interface UserService {
	
	public String saveUser(User user);
	
	public List<UserResponse> getAllUsers();
	
	public LoginResponse login(String loginId);
	
	public boolean forgotPassword(ForgotPasswordRequest request, String loginId);
	
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest);
	
	public List<UserResponse> searchUsers(String loginId);
	
	public Tag taggedTweets(String loginId);
	
	public void tagUser(String loginId, String tweetId);
	


}
