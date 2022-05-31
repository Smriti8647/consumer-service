package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.controller.UpdateController;
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

	public static final Logger LOGGER = LoggerFactory.getLogger(UpdateController.class);
	private final UserRepository userRepository;

//	@Autowired
//	UserRepository userRepository;
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	TagRepository tagRepository;

	@Override
	public String saveUser(User user) {
		Boolean isUserWithEmailPresent = userRepository.findUserByEmail(user.getEmail()).isPresent();
		Boolean isUserWithIdPresent = userRepository.findById(user.getLoginId()).isPresent();
		if (isUserWithIdPresent) {
			return "This login id is already registered";
		} else if (isUserWithEmailPresent) {
			return "This email id is already registered";
		} else {
			user.setRole("ROLE_USER");
			userRepository.save(user);
			return "Successful with id " + user.getLoginId();
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
		UserResponse userResponse = new UserResponse();
		userResponse.setAvtar(user.getAvtar());
		userResponse.setLoginId(user.getLoginId());
		userResponse.setName(user.getFirstName());
		return userResponse;
	}

	@Override
	public List<UserResponse> getAllUsers() {
		List<User> userList = userRepository.findAll();
		if (userList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'No user Present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("No user Present in Database");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching Users ", this.getClass().getSimpleName());
		}
		List<UserResponse> userResponseList = new ArrayList<>();
		userList.forEach(user -> {
			UserResponse userResposne = new UserResponse();
			userResposne = populateUserResponse(user);
			userResponseList.add(userResposne);
		});
		// userResposne=populateUserResponse()
		return userResponseList;
	}

	@Override
	public LoginResponse login(String loginId) {
		LoginResponse loginResponse = new LoginResponse();
		Optional<User> user = userRepository.findById(loginId);
		if (!user.isPresent()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'User not present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("User not present in Database");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching User ", this.getClass().getSimpleName());
		}
		populateLoginResponse(loginResponse, user.get());
		return loginResponse;
	}

	private void populateLoginResponse(LoginResponse loginResponse, User user) {
		loginResponse.setLoginId(user.getLoginId());
		loginResponse.setPassword(user.getPassword());
		loginResponse.setRole(user.getRole());
	}

	@Override
	public boolean forgotPassword(ForgotPasswordRequest request, String loginId) {
		Optional<User> user = userRepository.findById(loginId);
		if (!user.isPresent()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'User not present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("User not present in Database");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching User Details ", this.getClass().getSimpleName());
		}
		if (user.get().getQuestion() == request.getQues() && user.get().getAns() == request.getAns()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		Optional<User> user = userRepository.findById(updatePasswordRequest.getLoginId());
		if (!user.isPresent()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'User not present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("User not present in Database");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Updating Password ", this.getClass().getSimpleName());
		}
		user.get().setPassword(updatePasswordRequest.getNewPassword());
		userRepository.save(user.get());
	}

	@Override
	public List<UserResponse> searchUsers(String loginId) {
		List<User> userList = userRepository.findUsersByPartialId(loginId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching Users ", this.getClass().getSimpleName());
		}
		List<UserResponse> userResponseList = new ArrayList<>();
		userList.forEach(user -> {
			UserResponse userResposne = new UserResponse();
			userResposne = populateUserResponse(user);
			userResponseList.add(userResposne);
		});
		return userResponseList;
	}

	@Override
	public Tag taggedTweets(String loginId) {
		Optional<Tag> tag = tagRepository.findById(loginId);
		// TODO to decide what to do
//		if (!tag.isPresent()) {
//			if(LOGGER.isDebugEnabled()) {
//				LOGGER.debug("{}, Information: Throwing ResourceNotFoundException with message 'User not present in Database' ",this.getClass().getSimpleName());
//			}
//			throw new ResourceNotFoundException("User not present in Database");
//		}
		return tag.get();
	}

	@Override
	public void tagUser(String loginId, String tweetId) {
		Optional<Tag> tag = tagRepository.findById(loginId);
		if (tag.isEmpty()) {
			Tag tagRecord = new Tag();
			tagRecord.setLoginId(loginId);
			List<String> tweetIdList = new ArrayList<>();
			tweetIdList.add(tweetId);
			tagRecord.setTweetIdList(tweetIdList);
			tagRepository.insert(tagRecord);
		} else {
			List<String> tweetIdList = tag.get().getTweetIdList();
			tweetIdList.add(tweetId);
			tag.get().setTweetIdList(tweetIdList);
			tagRepository.save(tag.get());
		}
	}
}
