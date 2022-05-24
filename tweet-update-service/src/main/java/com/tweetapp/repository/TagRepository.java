package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

}
