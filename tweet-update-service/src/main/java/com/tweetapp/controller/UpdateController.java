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

import com.tweetapp.exceptions.ResourceAlreadyPresentException;
import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Comment;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.TagDto;
import com.tweetapp.model.Tweet;
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
			LOGGER.debug("{}, User Request: {}", this.getClass().getSimpleName(), user);
		}
		try {
			String res = userService.saveUser(user);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Succesfully registered user : {}", user);
			}
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (ResourceAlreadyPresentException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("User {} not registered", user);
			}
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

		}
	}

	@GetMapping("/all-users")
	public ResponseEntity<List<UserResponse>> allUsers() {

		List<UserResponse> userResponseList = userService.getAllUsers();
		HttpStatus status = HttpStatus.OK;
		if (userResponseList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Users not found", this.getClass().getSimpleName());
			}
			status = HttpStatus.NOT_FOUND;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Returning all users");
		}
		return new ResponseEntity<>(userResponseList, status);
	}

	@GetMapping("{loginId}/search-user")
	public ResponseEntity<List<UserResponse>> findUser(@PathVariable String loginId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Calling DB to fetch a user: {}", this.getClass().getSimpleName(), loginId);
		}
		return new ResponseEntity<>(userService.searchUsers(loginId), HttpStatus.OK);
	}

	@PostMapping("/{loginId}/forgot")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest,
			@PathVariable String loginId) {
		try {
			String result = userService.forgotPassword(forgotPasswordRequest, loginId);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>("User not Found", HttpStatus.NOT_FOUND);

		}
	}

	@GetMapping("{loginId}/login")
	public ResponseEntity<LoginResponse> login(@PathVariable String loginId) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			loginResponse = userService.login(loginId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Returning Login Response for {}", this.getClass().getSimpleName(), loginId);
			}
			return new ResponseEntity<>(loginResponse, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(loginResponse, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("tweets")
	public ResponseEntity<List<Tweet>> allTweets() {
		List<Tweet> tweetList = tweetService.getAllTweets();
		HttpStatus status = HttpStatus.OK;
		if (tweetList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, No Tweets found", this.getClass().getSimpleName());
			}
			status = HttpStatus.NOT_FOUND;
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Returning All Tweets", this.getClass().getSimpleName());
			}
		}
		return new ResponseEntity<>(tweetList, status);
	}

	@GetMapping("{loginId}/tweets")
	public ResponseEntity<List<Tweet>> userTweets(@PathVariable String loginId) {
		List<Tweet> tweetList = tweetService.getTweetByUsername(loginId);
		HttpStatus status = HttpStatus.OK;
		if (tweetList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, No Tweets found", this.getClass().getSimpleName());
			}
			status = HttpStatus.NOT_FOUND;
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Returning All Tweets", this.getClass().getSimpleName());
			}
		}
		return new ResponseEntity<>(tweetList, status);
	}

	@PostMapping("add-tweet")
	public ResponseEntity<String> addTweet(@RequestBody Tweet tweet) {
		tweetService.postTweet(tweet);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Successfully tweet added for user : {}", this.getClass().getSimpleName(),
					tweet.getLoginId());
		}
		return new ResponseEntity<>("Successfully tweet added for loginId " + tweet.getLoginId(), HttpStatus.CREATED);
	}

	@PutMapping("/update-tweet/{id}")
	public ResponseEntity<String> updateTweet(@RequestBody String updatedTweet, @PathVariable String id) {
		try {
			tweetService.updateTweet(id, updatedTweet);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Successfully updated tweet with id: {}", this.getClass().getSimpleName(), id);
			}
			return new ResponseEntity<>("Successfully updated Tweet ", HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{loginId}/delete-tweet/{id}")
	public ResponseEntity<String> deleteTweet(@PathVariable String loginId, @PathVariable String id) {
		tweetService.deleteTweet(loginId, id);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}, Successfully deleted tweet with id: {}", this.getClass().getSimpleName(), id);
		}
		return new ResponseEntity<>("Successfully deleted Tweet ", HttpStatus.OK);
	}

	@PutMapping("{loginId}/like/{id}")
	public ResponseEntity<String> likeTweet(@PathVariable String loginId, @PathVariable String id) {
		try {
			tweetService.likeTweet(loginId, id);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Successfully added like to a tweet with id: {}", this.getClass().getSimpleName(), id);
			}
			return new ResponseEntity<>("Tweet liked by " + loginId, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ResourceAlreadyPresentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}

	}

	@PutMapping("{loginId}/dislike/{id}")
	public ResponseEntity<String> dislikeTweet(@PathVariable String loginId, @PathVariable String id) {
		try {

			tweetService.dislikeTweet(loginId, id);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Successfully removed like from a tweet with id: {}", this.getClass().getSimpleName(),
						id);
			}
			return new ResponseEntity<>("Tweet disliked by " + loginId, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("reply/{id}")
	public ResponseEntity<String> replyTweet(@RequestBody Comment comment, @PathVariable String id) {
		try {
			tweetService.replyTweet(comment, id);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{}, Successfully added reply to a tweet with id: {}", this.getClass().getSimpleName(),
						id);
			}
			return new ResponseEntity<>("Successfully added reply to tweet ", HttpStatus.CREATED);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("{loginId}/tagged-tweets")
	public ResponseEntity<?> taggedTweets(@PathVariable String loginId) {
		TagDto tag = new TagDto();
		try {
			tag = userService.taggedTweets(loginId);
			return new ResponseEntity<>(tag, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(tag, HttpStatus.NOT_FOUND);
		} catch (ResourceAlreadyPresentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

}
