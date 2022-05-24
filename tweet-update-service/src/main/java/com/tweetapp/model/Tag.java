package com.tweetapp.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tag {
	@Id
	private String loginId;
	private List<String> tweetIdList;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public List<String> getTweetIdList() {
		return tweetIdList;
	}

	public void setTweetIdList(List<String> tweetIdList) {
		this.tweetIdList = tweetIdList;
	}

}
