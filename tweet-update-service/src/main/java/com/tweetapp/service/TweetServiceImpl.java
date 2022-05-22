package com.tweetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

@Service
public class TweetServiceImpl implements TweetService {

	@Autowired
	TweetRepository tweetRepository;
	
	public List<Tweet> getAllTweets(){
		List<Tweet> tweetList = tweetRepository.findAll();
		if(tweetList.isEmpty()) {
			throw new ResourceNotFoundException("No Tweets are present in Database");
		}
		return tweetList;
	}
	
	public List<Tweet> getTweetByUsername(String loginId){
		List<Tweet> tweetList = tweetRepository.findAllByLoginId(loginId);
		if(tweetList.isEmpty()) {
			throw new ResourceNotFoundException("No Tweets for this user are present in Database");
		}
		return tweetList;
	}
	
	public void postTweet(Tweet tweet) {
		tweetRepository.insert(tweet);
	}
}
