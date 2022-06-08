package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Comment;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TweetServiceTest {

	@InjectMocks
	public TweetServiceImpl tweetService;

	@MockBean
	public TweetRepository tweetRepository;

	@MockBean
	public Tweet tweet;

	@BeforeEach
	public void setUp() {
		tweetService = new TweetServiceImpl(tweetRepository);
	}

	public void setTweet() {
		tweet = new Tweet();
		tweet.setId("abc");
		tweet.setLoginId("sasha");
		tweet.setMessage("hey, anyone up for game?");
		tweet.setAvtar("avtar1");
		tweet.setTime(LocalDateTime.now());
		List<String> isLikeList = new ArrayList<>();
		isLikeList.add("sarah");
		tweet.setIsLikeList(isLikeList);
		Comment comment = new Comment();
		comment.setCommentMessage("yeah");
		comment.setCommentor("sarah");
		comment.setTime(LocalDateTime.now());
		List<Comment> commentList = new ArrayList<>();
		commentList.add(comment);
		tweet.setCommentList(commentList);
	}

	@Test
	public void testGetTweetByUsername() throws ResourceNotFoundException {
		setTweet();
		List<Tweet> tweetList = new ArrayList<>();
		tweetList.add(tweet);
		when(tweetRepository.findAllByLoginId("sarah")).thenReturn(tweetList);
		List<Tweet> tweetList1 = tweetService.getTweetByUsername("sarah");
		assertEquals(tweetList.get(0).getId(), tweetList1.get(0).getId());
	}

	@Test
	public void testGetAllTweets() throws ResourceNotFoundException {
		setTweet();
		List<Tweet> tweetList = new ArrayList<>();
		tweetList.add(tweet);
		when(tweetRepository.findAll()).thenReturn(tweetList);
		List<Tweet> tweetList1 = tweetService.getAllTweets();
		assertEquals(tweetList.get(0).getId(), tweetList1.get(0).getId());
	}

	@Test
	public void testUpdateTweet() throws ResourceNotFoundException {
		when(tweetRepository.findById("abc")).thenReturn(Optional.of(tweet));
		tweetService.updateTweet("abc", "heyyy");
	}
	
	@Test
	public void testUpdateTweet_ThrowsException() {
		when(tweetRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> tweetService.updateTweet("abc", "heyyy"));
	}

	@Test
	public void testLikeTweet() throws ResourceNotFoundException {
		setTweet();
		when(tweetRepository.findById("abc")).thenReturn(Optional.of(tweet));
		tweetService.likeTweet("sam", "abc");
		assertEquals(2, tweet.getIsLikeList().size());
	}

	@Test
	public void testLikeTweet_ThrowsException() {
		when(tweetRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> tweetService.likeTweet("sam", "abc"));
	}

	@Test
	public void testDislikeTweet() throws ResourceNotFoundException {
		setTweet();
		when(tweetRepository.findById("abc")).thenReturn(Optional.of(tweet));
		tweetService.dislikeTweet("sarah", "abc");
		assertEquals(0, tweet.getIsLikeList().size());
	}

	@Test
	public void testDisLikeTweet_ThrowsException() {
		when(tweetRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> tweetService.dislikeTweet("sam", "abc"));
	}

	@Test
	public void testReplyTweet() throws ResourceNotFoundException {
		setTweet();
		when(tweetRepository.findById("abc")).thenReturn(Optional.of(tweet));
		Comment comment = new Comment();
		comment.setCommentMessage("heya");
		comment.setCommentor("sam");
		comment.setTime(LocalDateTime.now());
		tweetService.replyTweet(comment, "abc");
		assertEquals(2, tweet.getCommentList().size());
	}

	@Test
	public void testReplyTweet_ThrowsException() {
		when(tweetRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> tweetService.replyTweet(new Comment(), "abc"));
	}
}
