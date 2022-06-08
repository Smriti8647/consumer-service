package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceAlreadyPresentException;
import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.ForgotPasswordRequest;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.Tag;
import com.tweetapp.model.TagDto;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.TagRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	public static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final TagRepository tagRepository;

	// @Value("kafka-topic")
	private static final String TOPIC = "tweetTag";
	
	private String noUserMsg="User not present in Database";

	@Autowired
	public UserServiceImpl(UserRepository userRepository,TagRepository tagRepository) {
		this.userRepository = userRepository;
		this.tagRepository=tagRepository;
	}

	@Override
	public String saveUser(User user) {
		Boolean isUserWithEmailPresent = userRepository.findUserByEmail(user.getEmail()).isPresent();
		Boolean isUserWithIdPresent = userRepository.findById(user.getLoginId()).isPresent();
		if (isUserWithIdPresent.booleanValue()) {
			throw new ResourceAlreadyPresentException("The loginId "+user.getLoginId()+" is already registered");
		} else if (isUserWithEmailPresent.booleanValue()) {
			throw new ResourceAlreadyPresentException("The emailId "+user.getEmail()+" is already registered");
		} else {
			user.setRole("ROLE_USER");
			userRepository.save(user);
			return "Successful with id " + user.getLoginId();
		}
	}

	private UserResponse populateUserResponse(User user) {
		UserResponse userResponse = new UserResponse();
		userResponse.setAvtar(user.getAvtar());
		userResponse.setLoginId(user.getLoginId());
		userResponse.setName(user.getFirstName()+" "+user.getLastName());
		return userResponse;
	}

	@Override
	public List<UserResponse> getAllUsers() {
		List<User> userList = userRepository.findAll();
		List<UserResponse> userResponseList = new ArrayList<>();
		if (userList.isEmpty()) {
			return userResponseList;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching Users from DB", this.getClass().getSimpleName());
		}

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
			throw new ResourceNotFoundException(noUserMsg);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching User from DB", this.getClass().getSimpleName());
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
	public String forgotPassword(ForgotPasswordRequest request, String loginId) {
		Optional<User> user = userRepository.findById(loginId);
		if (!user.isPresent()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Information: 'User not present in Database' ", this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noUserMsg);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Information: Fetching User Details from DB", this.getClass().getSimpleName());
		}
		if (user.get().getQuestion().equalsIgnoreCase(request.getQues())  && user.get().getAns().equalsIgnoreCase(request.getAns())) {
			user.get().setPassword(request.getNewPassword());
			userRepository.save(user.get());
			return "Succesfully Updated Passwoed";
		} else {
			return "Request failed, question/answer does not match";
		}
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
	public TagDto taggedTweets(String loginId) {
		Optional<TagDto> tag = tagRepository.findById(loginId);
		if (!tag.isPresent()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'User not present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noUserMsg);
		}
		return tag.get();
	}

	@KafkaListener(topics = TOPIC, groupId = "group_id", containerFactory = "userKafkaListenerFactory")
	public void consumeJson(Tag tag) {
		List<String> userList = tag.getUsers();
		for (String user : userList) {
			Optional<TagDto> tagDto = tagRepository.findById(user);
			if (!tagDto.isPresent()) {
				TagDto tagRecord = new TagDto();
				tagRecord.setUser(user);
				List<String> tweetIdList = new ArrayList<>();
				tweetIdList.add(tag.getTweetId());
				tagRecord.setTweetId(tweetIdList);
				tagRepository.insert(tagRecord);
			} else {
				List<String> tweetIdList = tagDto.get().getTweetId();
				if(tweetIdList.contains(tag.getTweetId())) {
					throw new ResourceAlreadyPresentException("user  "+user+" is already tagged in tweet "+tag.getTweetId());
				}
				tweetIdList.add(tag.getTweetId());
				tagDto.get().setTweetId(tweetIdList);
				tagRepository.save(tagDto.get());
			}
		}
	}

}
