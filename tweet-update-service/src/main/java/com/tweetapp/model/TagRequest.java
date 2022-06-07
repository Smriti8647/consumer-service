package com.tweetapp.model;

import java.util.List;


public class TagRequest {
	
	private String tweetId;
	private List<String> users;

	public TagRequest() {
		super();
	}

	public TagRequest(String tweetId, List<String> users) {
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
