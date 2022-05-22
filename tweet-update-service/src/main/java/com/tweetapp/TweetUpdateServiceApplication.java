package com.tweetapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@SpringBootApplication
@EnableMongoRepositories
public class TweetUpdateServiceApplication {
 
//	@Autowired
//	UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(TweetUpdateServiceApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		// TODO Auto-generated method stub
//		User user =new User();
//		user.setFirstName("Smriti");
//		user.setLastName("Arora");
//		user.setLoginId("smriti");
//		user.setPassword("2602");
//		user.setEmail("smriti8647@gmail.com");
//		user.setQuestion("primary school");
//		user.setAns("dyal singh pulic school");
//		user.setContactNumber("9815253922");
//		user.setAvtar("avtar1");
//		userRepository.insert(user);
//		
//	}

}
