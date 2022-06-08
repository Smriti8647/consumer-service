package com.tweetapp.model;

import java.time.LocalDateTime;

public class Comment {

	private String commentMessage;
	private String commentor;
	private LocalDateTime time;

	public String getCommentMessage() {
		return commentMessage;
	}

	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}

	public String getCommentor() {
		return commentor;
	}

	public void setCommentor(String commentor) {
		this.commentor = commentor;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime localDateTime) {
		this.time = localDateTime;
	}

}
