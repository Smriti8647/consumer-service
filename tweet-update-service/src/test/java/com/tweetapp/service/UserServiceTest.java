package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.UpdatePasswordRequest;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	public UserServiceImpl userService;

	@MockBean
	public UserRepository userRepository;

	@Mock
	public User user;

	@BeforeEach
	public void setUp() {
		userService = new UserServiceImpl(userRepository);
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
	public void testLogin() throws ResourceNotFoundException {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		LoginResponse loginResponse1 = userService.login(user.getLoginId());
		assertNotNull(loginResponse1);
	}

	@Test
	public void testLogin_throwsException() {
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> userService.login(user.getLoginId()));
	}

	@Test
	public void testGetAllUsers() throws ResourceNotFoundException {
		setUser();
		List<User> userList = new ArrayList<>();
		userList.add(user);
		when(userRepository.findAll()).thenReturn(userList);
		List<UserResponse> userResponseList = userService.getAllUsers();
		assertNotNull(userResponseList);
	}

	@Test
	public void testGetAllUsers_throwsException() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());
		assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
	}

	@Test
	public void testSearchUser_UsersPresent() {
		setUser();
		List<User> userList = new ArrayList<>();
		userList.add(user);
		when(userRepository.findUsersByPartialId("sas")).thenReturn(userList);
		List<UserResponse> userResponseList = userService.searchUsers("sas");
		assertFalse(userResponseList.isEmpty());
	}

	@Test
	public void testSearchUser_NoUserPresent() {
		when(userRepository.findUsersByPartialId(anyString())).thenReturn(Collections.emptyList());
		List<UserResponse> userResponseList = userService.searchUsers("sas");
		assertTrue(userResponseList.isEmpty());
	}

	@Test
	public void updatePassword_SuccessfullyUpdated() throws ResourceNotFoundException {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
		updatePasswordRequest.setLoginId("sasha");
		updatePasswordRequest.setNewPassword("newPass");
		userService.updatePassword(updatePasswordRequest);
		assertEquals("newPass", user.getPassword());
	}

	@Test
	public void updatePassword_throwsException() {
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());
		UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
		assertThrows(ResourceNotFoundException.class, () -> userService.updatePassword(updatePasswordRequest));
	}

	@Test
	public void testSaveUser_EmailAlreadyPresent() {
		setUser();
		when(userRepository.findUserByEmail("qwerty@gmail.com")).thenReturn(Optional.of(user));
		String msg = userService.saveUser(user);
		assertEquals("this email id is already registered", msg);
	}

	@Test
	public void testSaveUser_loginIdAlreadyPresent() {
		setUser();
		when(userRepository.findById("sasha")).thenReturn(Optional.of(user));
		String msg = userService.saveUser(user);
		assertEquals("this login id is already registered", msg);
	}

	@Test
	public void testSaveUser() {
		setUser();
		String msg = userService.saveUser(user);
		assertEquals("successful with id " + user.getLoginId(), msg);
	}
}
