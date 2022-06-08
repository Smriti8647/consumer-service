package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tweetapp.model.Comment;
import com.tweetapp.model.ForgotPasswordRequest;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;

@SpringBootTest
class UpdateControllerTest {

	@InjectMocks
	UpdateController updateController;

	@Mock
	UserService userService;

	@Mock
	TweetService tweetService;
	private MockMvc mockMvc;

//	private String mapToJson(Object obj) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		return objectMapper.writeValueAsString(obj);
//	}

	@BeforeEach
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(updateController).build();
	}

	@Test
	void testGetAllUsers_NotFound() throws Exception {
		List<UserResponse> userResponseList = new ArrayList<>();
		when(userService.getAllUsers()).thenReturn(userResponseList);
		mockMvc.perform(get("/all-users")).andExpect(status().isNotFound());
	}

	@Test
	void testSearchUser() throws Exception {
		mockMvc.perform(get("/smriti/search-user")).andExpect(status().isOk());
	}

	@Test
	void testForgotPassword() throws Exception {
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setQues("primary school");
		forgotPasswordRequest.setAns("dsps");
		when(userService.forgotPassword(forgotPasswordRequest, "sasha")).thenReturn("");
		ResponseEntity<String> object = updateController.forgotPassword(forgotPasswordRequest, "sasha");
		assertNotNull(object);
	}

	@Test
	void testLogin() throws Exception {
		mockMvc.perform(get("/sasha/login")).andExpect(status().isOk());
	}

	@Test
	void testGetAllTweets() throws Exception {
		List<Tweet> tweetList = new ArrayList<>();
		when(tweetService.getAllTweets()).thenReturn(tweetList);
		mockMvc.perform(get("/tweets")).andExpect(status().isNotFound());
	}

	@Test
	void testGetUserTweets_NotFound() throws Exception {
		List<Tweet> tweetList = new ArrayList<>();
		when(tweetService.getTweetByUsername("sasha")).thenReturn(tweetList);
		mockMvc.perform(get("/sasha/tweets")).andExpect(status().isNotFound());
	}

	@Test
	void testRegisterUser() throws Exception {
		User user = new User();
		user.setLoginId("sasha");
		user.setFirstName("Sasha");
		user.setLastName("Muller");
		user.setContactNumber("1234567890");
		user.setEmail("qwerty@gmail.com");
		user.setAvtar("avtar1");
		user.setPassword("asdfgh");
		user.setQuestion("primary school");
		user.setAns("dyal singh public school");
		when(userService.saveUser(user)).thenReturn("Successful with id " + user.getLoginId());
		ResponseEntity<String> object = updateController.registerUser(user);
		assertNotNull(object);
	}

	@Test
	void testAddTweet() throws Exception {
		Tweet tweet = new Tweet();
		tweet.setId("abc");
		tweet.setLoginId("sasha");
		tweet.setMessage("hey, anyone up for game?");
		tweet.setAvtar("avtar1");
		tweet.setTime(LocalDateTime.now());
		List<String> isLikeList = new ArrayList<>();
		isLikeList.add("sarah");
		tweet.setIsLikeList(isLikeList);
		Comment comment = new Comment();
		comment.setCommentMessage("yeah");
		comment.setCommentor("sarah");
		comment.setTime(LocalDateTime.now());
		List<Comment> commentList = new ArrayList<>();
		commentList.add(comment);
		tweet.setCommentList(commentList);
		ResponseEntity<String> object = updateController.addTweet(tweet);
		assertNotNull(object);

	}

	@Test
	void testUpdateTweet() throws Exception {
		ResponseEntity<String> object = updateController.updateTweet("hey", "abc");
		assertNotNull(object);
	}

	@Test
	void testDeleteTweet() throws Exception {
		ResponseEntity<String> object = updateController.deleteTweet("sasha", "abc");
		assertNotNull(object);
	}

	@Test
	void testLikeTweet() throws Exception {
		ResponseEntity<String> object = updateController.likeTweet("sasha", "abc");
		assertNotNull(object);
	}

	@Test
	void testDislikeTweet() throws Exception {
		ResponseEntity<String> object = updateController.dislikeTweet("sasha", "abc");
		assertNotNull(object);
	}

	@Test
	void tesReplyTweet() throws Exception {
		Comment comment = new Comment();
		comment.setCommentMessage("heya");
		comment.setCommentor("sasha");
		comment.setTime(LocalDateTime.now());
		ResponseEntity<String> object = updateController.replyTweet(comment, "abc");
		assertNotNull(object);
	}

}
