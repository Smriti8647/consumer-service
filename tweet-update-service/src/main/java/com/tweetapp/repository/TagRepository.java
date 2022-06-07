package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.TagDto;

public interface TagRepository extends MongoRepository<TagDto, String> {

}
