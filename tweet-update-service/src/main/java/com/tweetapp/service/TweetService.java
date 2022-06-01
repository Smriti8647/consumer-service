package com.tweetapp.service;

import java.util.List;

import com.tweetapp.model.Comment;
import com.tweetapp.model.Tweet;

public interface TweetService {

	public List<Tweet> getAllTweets();

	public List<Tweet> getTweetByUsername(String loginId);

	public void postTweet(Tweet tweet);

	public void updateTweet(String id, String updatedTweet);

	public void deleteTweet(String loginId, String id);

	public void likeTweet(String loginId, String id);

	public void dislikeTweet(String loginId, String id);

	public void replyTweet(Comment comment, String id);
}
