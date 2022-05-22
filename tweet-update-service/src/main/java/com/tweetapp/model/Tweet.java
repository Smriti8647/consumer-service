package com.tweetapp.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tweet {
	
	@Id
	private String loginId;
	private String avtar;
	private String message;
	private LocalDateTime time;
	private boolean isLike;
	private Integer noOfLikes;
	private List<Comment> commentList;
	public String getAvtar() {
		return avtar;
	}
	public void setAvtar(String avtar) {
		this.avtar = avtar;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime localDateTime) {
		this.time = localDateTime;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	public Integer getNoOfLikes() {
		return noOfLikes;
	}
	public void setNoOfLikes(Integer noOfLikes) {
		this.noOfLikes = noOfLikes;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	
	

}
