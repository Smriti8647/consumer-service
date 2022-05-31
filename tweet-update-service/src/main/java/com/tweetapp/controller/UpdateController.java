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
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("User Request: {}",user);
		}
		String res = userService.saveUser(user);
//		return new ResponseEntity<>("Succesfully registerd the user with loginId"+user.getLoginId(),HttpStatus.OK);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/all-users")
	public List<UserResponse> allUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("{loginId}/search-user")
	public List<UserResponse> findUser(@PathVariable String loginId) {
		return userService.searchUsers(loginId);
	}

//	@GetMapping("/{loginId}/user")
//	public UserResponse singleUser(@PathVariable String loginId) {
//		System.out.println("loginId");
//		return userService.getUser(loginId);
//	}

	@GetMapping("/{loginId}/forgot")
	public Boolean forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest,
			@PathVariable String loginId) {
		return userService.forgotPassword(forgotPasswordRequest, loginId);
	}

	@PutMapping("/update-Password")
	public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
		userService.updatePassword(updatePasswordRequest);
		return new ResponseEntity<>("Successfully changed password for loginId " + updatePasswordRequest.getLoginId(),
				HttpStatus.OK);
	}

	@GetMapping("{loginId}/login")
	public LoginResponse login(@PathVariable String loginId) {
		return userService.login(loginId);
	}

	@GetMapping("tweets")
	public List<Tweet> tweets() {
		return tweetService.getAllTweets();
	}

	@GetMapping("{loginId}/tweets")
	public List<Tweet> tweets(@PathVariable String loginId) {
		return tweetService.getTweetByUsername(loginId);
	}

	@PostMapping("add-tweet")
	public ResponseEntity<String> addTweet(@RequestBody Tweet tweet) {
		tweetService.postTweet(tweet);
		return new ResponseEntity<>("Successfully tweet added for loginId " + tweet.getId(), HttpStatus.CREATED);
	}

	@PutMapping("{loginId}/update-tweet/{id}")
	public ResponseEntity<String> updateTweet(@RequestBody String updatedTweet, @PathVariable String loginId,
			@PathVariable String id) {
		tweetService.updateTweet(loginId, id, updatedTweet);
		return new ResponseEntity<>("Successfully updated Tweet ", HttpStatus.OK);
	}

	@DeleteMapping("{loginId}/delete-tweet/{id}")
	public ResponseEntity<String> deleteTweet(@PathVariable String loginId, @PathVariable String id) {
		tweetService.deleteTweet(loginId, id);
		return new ResponseEntity<>("Successfully deleted Tweet ", HttpStatus.OK);
	}

	@PutMapping("{loginId}/like/{id}")
	public ResponseEntity<String> likeTweet(@PathVariable String loginId, @PathVariable String id) {
		tweetService.likeTweet(loginId, id);
		return new ResponseEntity<>("Tweet liked by " + loginId, HttpStatus.OK);
	}

	@PutMapping("{loginId}/dislike/{id}")
	public ResponseEntity<String> dislikeTweet(@PathVariable String loginId, @PathVariable String id) {
		tweetService.dislikeTweet(loginId, id);
		return new ResponseEntity<>("Tweet disliked by " + loginId, HttpStatus.OK);
	}

	@PostMapping("reply/{id}")
	public ResponseEntity<String> replyTweet(@RequestBody Comment comment, @PathVariable String id) {
		tweetService.replyTweet(comment, id);
		return new ResponseEntity<>("Successfully added reply to tweet ", HttpStatus.CREATED);
	}

	@GetMapping("{loginId}/tagged-tweets")
	public Tag taggedTweets(@PathVariable String loginId) {
		return userService.taggedTweets(loginId);
	}

	@PutMapping("{loginId}/tag/{tweetId}")
	public ResponseEntity<String> tagUser(@PathVariable String loginId, @PathVariable String tweetId) {
		userService.tagUser(loginId, tweetId);
		return new ResponseEntity<>("Successfully tagged user " + loginId, HttpStatus.CREATED);
	}

}
