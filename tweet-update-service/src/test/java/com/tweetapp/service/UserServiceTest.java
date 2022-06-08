package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tweetapp.exceptions.ResourceAlreadyPresentException;
import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.ForgotPasswordRequest;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.TagDto;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.TagRepository;
import com.tweetapp.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

	@InjectMocks
	public UserServiceImpl userService;

	@MockBean
	public UserRepository userRepository;

	@MockBean
	public TagRepository tagRepository;

	@Mock
	public User user;

	@BeforeEach
	public void setUp() {
		userService = new UserServiceImpl(userRepository, tagRepository);
	}

	public void setUser() {
		user = new User();
		user.setLoginId("sasha");
		user.setFirstName("Sasha");
		user.setLastName("Muller");
		user.setContactNumber("1234567890");
		user.setEmail("qwerty@gmail.com");
		user.setAvtar("avtar1");
		user.setPassword("asdfgh");
		user.setQuestion("primary school");
		user.setAns("dyal singh public school");
		user.setRole("ROLE_USER");
	}

	@Test
	void testLogin() throws ResourceNotFoundException {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		LoginResponse loginResponse1 = userService.login(user.getLoginId());
		assertNotNull(loginResponse1);
	}

	@Test
	void testLogin_throwsException() {
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> userService.login(user.getLoginId()));
	}

	@Test
	void testGetAllUsers() throws ResourceNotFoundException {
		setUser();
		List<User> userList = new ArrayList<>();
		userList.add(user);
		when(userRepository.findAll()).thenReturn(userList);
		List<UserResponse> userResponseList = userService.getAllUsers();
		assertNotNull(userResponseList);
	}

	@Test
	void testSearchUser_UsersPresent() {
		setUser();
		List<User> userList = new ArrayList<>();
		userList.add(user);
		when(userRepository.findUsersByPartialId("sas")).thenReturn(userList);
		List<UserResponse> userResponseList = userService.searchUsers("sas");
		assertFalse(userResponseList.isEmpty());
	}

	@Test
	void testSearchUser_NoUserPresent() {
		when(userRepository.findUsersByPartialId(anyString())).thenReturn(Collections.emptyList());
		List<UserResponse> userResponseList = userService.searchUsers("sas");
		assertTrue(userResponseList.isEmpty());
	}

	@Test
	void forgotPassword_SuccessfullyUpdated() throws ResourceNotFoundException {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setQues("primary school");
		forgotPasswordRequest.setAns("dyal singh public school");
		forgotPasswordRequest.setNewPassword("newPass");
		userService.forgotPassword(forgotPasswordRequest, "sasha");
		assertEquals("newPass", user.getPassword());
	}

	@Test
	void forgotPassword_returnFalse() throws ResourceNotFoundException {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setQues("primary school");
		forgotPasswordRequest.setAns("dsps");
		forgotPasswordRequest.setNewPassword("newPass");
		String res = userService.forgotPassword(forgotPasswordRequest, "sasha");
		assertEquals("Request failed, question/answer does not match", res);
	}

	@Test
	void forgotPassword_throwsException() {
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		assertThrows(ResourceNotFoundException.class, () -> userService.forgotPassword(forgotPasswordRequest, "sasha"));
	}

	@Test
	void testSaveUser_EmailAlreadyPresent() {
		setUser();
		when(userRepository.findUserByEmail("qwerty@gmail.com")).thenReturn(Optional.of(user));
		assertThrows(ResourceAlreadyPresentException.class, () -> userService.saveUser(user));
	}

	@Test
	void testSaveUser_loginIdAlreadyPresent() {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		assertThrows(ResourceAlreadyPresentException.class, () -> userService.saveUser(user));
	}

	@Test
	void testSaveUser() {
		setUser();
		String msg = userService.saveUser(user);
		assertEquals("Successful with id " + user.getLoginId(), msg);
	}

	@Test
	void taggedTweets_throwsException() {
		when(tagRepository.findById("sasha")).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> userService.taggedTweets("sasha"));
	}

	@Test
	void taggedTweets() {
		TagDto tag = new TagDto();
		tag.setUser("sasha");
		List<String> tweetList = new ArrayList<>();
		tweetList.add("abc");
		tag.setTweetId(tweetList);
		when(tagRepository.findById("sasha")).thenReturn(Optional.of(tag));
		TagDto res = userService.taggedTweets("sasha");
		assertNotNull(res);
	}

}
