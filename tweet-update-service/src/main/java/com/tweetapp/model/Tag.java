package com.tweetapp.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document
public class Tag {
	//@Id
	private String tweetId;
	private List<String> users;

	public Tag() {
		super();
	}

	public Tag(String tweetId, List<String> users) {
		super();
		this.tweetId = tweetId;
		this.users = users;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Tag [tweetId=" + tweetId + ", users=" + users + "]";
	}

}
