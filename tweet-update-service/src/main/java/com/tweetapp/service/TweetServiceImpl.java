package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Comment;
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
	
	public void updateTweet(String loginId, String id, String updatedTweet) {
		Tweet tweet= tweetRepository.findByLoginIdAndId(loginId,id);
		tweet.setMessage(updatedTweet);
		tweetRepository.save(tweet);
	}
	
	public void deleteTweet(String loginId, String id) {
		tweetRepository.deleteById(id);
	}
	
	public void likeTweet(String loginId, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty()) {
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		List<String> isLikeList = tweet.get().getIsLikeList();
		isLikeList.add(loginId);
		tweet.get().setIsLikeList(isLikeList);
		tweetRepository.save(tweet.get());
	}
	
	public void dislikeTweet(String loginId, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty()) {
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		List<String> isLikeList = tweet.get().getIsLikeList();
		isLikeList.remove(loginId);
		tweet.get().setIsLikeList(isLikeList);
		tweetRepository.save(tweet.get());
	}
	
	public void replyTweet(Comment comment, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty()) {
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		List<Comment> commentList=tweet.get().getCommentList();
		commentList.add(comment);
		tweet.get().setCommentList(commentList);
		tweetRepository.save(tweet.get());
	}
	
}
