package com.tweetapp.controller;

import java.util.List;

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
	
//	@GetMapping("{loginId}/forgot")
//	public LoginResponse forgotPassword(@PathVariable String loginId){
//		
//	}
	
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
	
	@PostMapping("add")
	public ResponseEntity<String> addTweet(@RequestBody Tweet tweet){
		tweetService.postTweet(tweet);
		return new ResponseEntity<>("Successfully tweet added for loginId "+tweet.getLoginId(),HttpStatus.OK);
	}
	
	@PutMapping("{loginId}/update/{id}")
	public ResponseEntity<String> updateTweet(@RequestBody String updatedTweet, @PathVariable String loginId ,@PathVariable String id){
		tweetService.updateTweet(loginId, id, updatedTweet);
		return new ResponseEntity<>("Successfully updated Tweet ",HttpStatus.OK);
	}
	
	@DeleteMapping("{loginId}/delete/{id}")
	public ResponseEntity<String> deleteTweet(@PathVariable String loginId ,@PathVariable String id){
		tweetService.deleteTweet(loginId, id);
		return new ResponseEntity<>("Successfully deleted Tweet ",HttpStatus.OK);
	}
	
	@PutMapping("{loginId}/like/{id}")
	public ResponseEntity<String> likeTweet(@PathVariable String loginId ,@PathVariable String id){
		tweetService.likeTweet(loginId, id);
		return new ResponseEntity<>("Tweet liked by "+loginId,HttpStatus.OK);
	}
	
	@PutMapping("{loginId}/dislike/{id}")
	public ResponseEntity<String> dislikeTweet(@PathVariable String loginId ,@PathVariable String id){
		tweetService.dislikeTweet(loginId, id);
		return new ResponseEntity<>("Tweet disliked by "+loginId,HttpStatus.OK);
	}
	
	@PostMapping("reply/{id}")
	public ResponseEntity<String> replyTweet(@RequestBody Comment comment,@PathVariable String id){
		tweetService.replyTweet(comment,id);
		return new ResponseEntity<>("Successfully added reply to tweet ",HttpStatus.OK);
	}
	
}
