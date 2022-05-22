package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.service.TweetServiceImpl;
import com.tweetapp.service.UserServiceImpl;

@RestController
public class UpdateController{

	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	TweetServiceImpl tweetService;

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		String res=userService.saveUser(user); 
//		return new ResponseEntity<>("Succesfully registerd the user with loginId"+user.getLoginId(),HttpStatus.OK);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@GetMapping("/allusers")
	public List<UserResponse> allUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/user/{loginId}")
	public UserResponse singleUser(@PathVariable String loginId) {
		System.out.println("loginId");
		return userService.getUser(loginId);
	}
	
	@GetMapping("login/{loginId}")
	public LoginResponse login(@PathVariable String loginId){
		return userService.login(loginId);
	}
	
	@GetMapping("tweets")
	public List<Tweet> tweets(){
		return tweetService.getAllTweets();
	}
	
	@GetMapping("tweets/{loginId}")
	public List<Tweet> tweets(@PathVariable String loginId){
		return tweetService.getTweetByUsername(loginId);	
	}
	
	@PostMapping("addtweet/{loginId}")
	public ResponseEntity<String> addTweet(@RequestBody Tweet tweet){
		tweetService.postTweet(tweet);
		return new ResponseEntity<>("Successfully tweet added for loginId "+tweet.getLoginId(),HttpStatus.OK);
	}
}
