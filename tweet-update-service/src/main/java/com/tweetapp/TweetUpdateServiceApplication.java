package com.tweetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TweetUpdateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetUpdateServiceApplication.class, args);
	}

}
