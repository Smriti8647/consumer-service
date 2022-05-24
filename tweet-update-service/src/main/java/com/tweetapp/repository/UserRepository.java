package com.tweetapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tweetapp.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findUserByEmail(String email);

	@Query("{$or:[{firstName: {$regex: /?0/,$options:$i}},{lastName: {$regex: /?0/,$options:$i}},{loginId: {$regex: /?0/,$options:$i }}]}")
	List<User> findUsersByPartialId(String loginId);

}
