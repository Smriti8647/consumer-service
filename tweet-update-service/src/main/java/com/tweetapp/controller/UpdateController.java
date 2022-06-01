package com.tweetapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Comment;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.Tag;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.UpdatePasswordRequest;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;
import com.tweetapp.model.ForgotPasswordRequest;

@RestController
public class UpdateController {

	public static final Logger LOGGER = LoggerFactory.getLogger(UpdateController.class);

	@Autowired
	UserService userService;

	@Autowired
	TweetService tweetService;

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User Request: {}", user);
		}
		String res = userService.saveUser(user);
		if (res.startsWith("Successful with id")) {
			System.out.println("regsiterUser");
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} else {
			System.out.println("regsiterUser");
			return new ResponseEntity<>(res, HttpStatus.CONFLICT);

		}
	}

	@GetMapping("/all-users")
	public ResponseEntity<List<UserResponse>> allUsers() {
		List<UserResponse> userResponseList = userService.getAllUsers();
		HttpStatus status = HttpStatus.OK;
		if (userResponseList.isEmpty())
			status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(userResponseList, status);
	}

	@GetMapping("{loginId}/search-user")
	public ResponseEntity<List<UserResponse>> findUser(@PathVariable String loginId) {
		return new ResponseEntity<>(userService.searchUsers(loginId), HttpStatus.OK);
	}

//	@GetMapping("/{loginId}/user")
//	public UserResponse singleUser(@PathVariable String loginId) {
//		System.out.println("loginId");
//		return userService.getUser(loginId);
//	}

	// ResourceNotFoundException
	@GetMapping("/{loginId}/forgot")
	public ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest,
			@PathVariable String loginId) {
		try {
			Boolean result = userService.forgotPassword(forgotPasswordRequest, loginId);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

		}
	}

	@PutMapping("/update-Password")
	public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
		userService.updatePassword(updatePasswordRequest);
		try {
			userService.updatePassword(updatePasswordRequest);
			return new ResponseEntity<>(
					"Successfully changed password for loginId " + updatePasswordRequest.getLoginId(), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("{loginId}/login")
	public ResponseEntity<LoginResponse> login(@PathVariable String loginId) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			loginResponse = userService.login(loginId);
			return new ResponseEntity<>(loginResponse, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(loginResponse, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("tweets")
	public ResponseEntity<List<Tweet>> allTweets() {
		List<Tweet> tweetList = tweetService.getAllTweets();
		HttpStatus status = HttpStatus.OK;
		if (tweetList.isEmpty())
			status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(tweetList, status);
	}

	@GetMapping("{loginId}/tweets")
	public ResponseEntity<List<Tweet>> userTweets(@PathVariable String loginId) {
		List<Tweet> tweetList = tweetService.getTweetByUsername(loginId);
		HttpStatus status = HttpStatus.OK;
		if (tweetList.isEmpty())
			status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(tweetList, status);
	}

	@PostMapping("add-tweet")
	public ResponseEntity<String> addTweet(@RequestBody Tweet tweet) {
		tweetService.postTweet(tweet);
		return new ResponseEntity<>("Successfully tweet added for loginId " + tweet.getId(), HttpStatus.CREATED);
	}

	@PutMapping("/update-tweet/{id}")
	public ResponseEntity<String> updateTweet(@RequestBody String updatedTweet, @PathVariable String id) {
		try {
			tweetService.updateTweet(id, updatedTweet);
			return new ResponseEntity<>("Successfully updated Tweet ", HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{loginId}/delete-tweet/{id}")
	public ResponseEntity<String> deleteTweet(@PathVariable String loginId, @PathVariable String id) {
		tweetService.deleteTweet(loginId, id);
		return new ResponseEntity<>("Successfully deleted Tweet ", HttpStatus.OK);
	}

	@PutMapping("{loginId}/like/{id}")
	public ResponseEntity<String> likeTweet(@PathVariable String loginId, @PathVariable String id) {
		try {
			tweetService.likeTweet(loginId, id);
			return new ResponseEntity<>("Tweet liked by " + loginId, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping("{loginId}/dislike/{id}")
	public ResponseEntity<String> dislikeTweet(@PathVariable String loginId, @PathVariable String id) {
		try {
			tweetService.dislikeTweet(loginId, id);
			return new ResponseEntity<>("Tweet disliked by " + loginId, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("reply/{id}")
	public ResponseEntity<String> replyTweet(@RequestBody Comment comment, @PathVariable String id) {
		try {
			tweetService.replyTweet(comment, id);
			return new ResponseEntity<>("Successfully added reply to tweet ", HttpStatus.CREATED);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("{loginId}/tagged-tweets")
	public ResponseEntity<Tag> taggedTweets(@PathVariable String loginId) {
		return new ResponseEntity<>(userService.taggedTweets(loginId), HttpStatus.OK);
	}

	@PutMapping("{loginId}/tag/{tweetId}")
	public ResponseEntity<String> tagUser(@PathVariable String loginId, @PathVariable String tweetId) {
		userService.tagUser(loginId, tweetId);
		return new ResponseEntity<>("Successfully tagged user " + loginId, HttpStatus.CREATED);
	}

}
