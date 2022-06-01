package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.controller.UpdateController;
import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Comment;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

@Service
public class TweetServiceImpl implements TweetService {

	public static final Logger LOGGER = LoggerFactory.getLogger(UpdateController.class);
//	@Autowired
//	TweetRepository tweetRepository;

	private final TweetRepository tweetRepository;

	@Autowired
	public TweetServiceImpl(TweetRepository tweetRepository) {
		this.tweetRepository = tweetRepository;
	}

	@Override
	public List<Tweet> getAllTweets() {
		List<Tweet> tweetList = tweetRepository.findAll();
		return tweetList;
	}

	@Override
	public List<Tweet> getTweetByUsername(String loginId) {
		List<Tweet> tweetList = tweetRepository.findAllByLoginId(loginId);
		return tweetList;
	}

	@Override
	public void postTweet(Tweet tweet) {
		tweetRepository.insert(tweet);
	}

	@Override
	public void updateTweet(String id, String updatedTweet) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'No Tweet with this id is present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		tweet.get().setMessage(updatedTweet);
		tweetRepository.save(tweet.get());
	}

	@Override
	public void deleteTweet(String loginId, String id) {
		tweetRepository.deleteById(id);
	}

	@Override
	public void likeTweet(String loginId, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'No Tweet with this id is present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		if (tweet.get().getIsLikeList() == null) {
			List<String> isLikeList = new ArrayList<>();
			isLikeList.add(loginId);
			tweet.get().setIsLikeList(isLikeList);
			tweetRepository.save(tweet.get());
		} else {
			System.out.println("like tweet else part");
			List<String> isLikeList = tweet.get().getIsLikeList();
			isLikeList.add(loginId);
			tweet.get().setIsLikeList(isLikeList);
			tweetRepository.save(tweet.get());
		}

	}

	@Override
	public void dislikeTweet(String loginId, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'No Tweet with this id is present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		List<String> isLikeList = tweet.get().getIsLikeList();
		isLikeList.remove(loginId);
		tweet.get().setIsLikeList(isLikeList);
		tweetRepository.save(tweet.get());
	}

	@Override
	public void replyTweet(Comment comment, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"{}, Information: Throwing ResourceNotFoundException with message 'No Tweet with this id is present in Database' ",
						this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException("No Tweet with this id is present in Database");
		}
		if (tweet.get().getCommentList() == null) {
			List<Comment> commentList = new ArrayList<>();
			commentList.add(comment);
			tweet.get().setCommentList(commentList);
			tweetRepository.save(tweet.get());
		} else {
			List<Comment> commentList = tweet.get().getCommentList();
			commentList.add(comment);
			tweet.get().setCommentList(commentList);
			tweetRepository.save(tweet.get());
		}
	}

}
