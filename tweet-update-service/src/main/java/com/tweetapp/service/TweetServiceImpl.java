package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exceptions.ResourceAlreadyPresentException;
import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Comment;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

@Service
public class TweetServiceImpl implements TweetService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TweetServiceImpl.class);

	private final TweetRepository tweetRepository;
	private String noTweetMsg = "No Tweet with this id is present in Database";
	private String exceptionMsg = "{}, Information: Throwing ResourceNotFoundException with message 'No Tweet with this id is present in Database' ";

	@Autowired
	public TweetServiceImpl(TweetRepository tweetRepository) {
		this.tweetRepository = tweetRepository;
	}

	@Override
	public List<Tweet> getAllTweets() {
		return tweetRepository.findAll();
	}

	@Override
	public List<Tweet> getTweetByUsername(String loginId) {
		return tweetRepository.findAllByLoginId(loginId);
	}

	@Override
	public void postTweet(Tweet tweet) {
		tweetRepository.insert(tweet);
	}

	@Override
	public void updateTweet(String id, String updatedTweet) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(exceptionMsg, this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noTweetMsg);
		}
		tweet.get().setMessage(updatedTweet);
		tweetRepository.save(tweet.get());
	}

	@Override
	public void deleteTweet(String loginId, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(exceptionMsg, this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noTweetMsg);
		}
		tweetRepository.deleteById(id);
	}

	@Override
	public void likeTweet(String loginId, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(exceptionMsg, this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noTweetMsg);
		}
		if (tweet.get().getIsLikeList() == null) {
			List<String> isLikeList = new ArrayList<>();
			isLikeList.add(loginId);
			tweet.get().setIsLikeList(isLikeList);
			tweetRepository.save(tweet.get());
		} else {
			List<String> isLikeList = tweet.get().getIsLikeList();
			if(isLikeList.contains(loginId)) {
				throw new ResourceAlreadyPresentException("Tweet id " + id + " is already liked by " + loginId);
			}
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
				LOGGER.debug(exceptionMsg, this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noTweetMsg);
		}
		List<String> isLikeList = tweet.get().getIsLikeList();
		if(isLikeList!=null && isLikeList.contains(loginId)) {
			isLikeList.remove(loginId);
			tweet.get().setIsLikeList(isLikeList);
			tweetRepository.save(tweet.get());
		}
		else {
			throw new ResourceNotFoundException("Tweet "+id+"is not liked by user "+loginId);
		}
		
	}

	@Override
	public void replyTweet(Comment comment, String id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(exceptionMsg, this.getClass().getSimpleName());
			}
			throw new ResourceNotFoundException(noTweetMsg);
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
