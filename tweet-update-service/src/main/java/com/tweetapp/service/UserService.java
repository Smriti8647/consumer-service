package com.tweetapp.service;

import java.util.List;

import com.tweetapp.model.ForgotPasswordRequest;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.TagDto;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;

public interface UserService {

	public String saveUser(User user);

	public List<UserResponse> getAllUsers();

	public LoginResponse login(String loginId);

	public String forgotPassword(ForgotPasswordRequest request, String loginId);

	public List<UserResponse> searchUsers(String loginId);

	public TagDto taggedTweets(String loginId);

}
