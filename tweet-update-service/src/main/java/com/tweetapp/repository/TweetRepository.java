package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.Tweet;

public interface TweetRepository extends MongoRepository<Tweet, String> {

	List<Tweet> findAllByLoginId(String loginId);

	Tweet findByLoginIdAndId(String loginId, String id);

}
