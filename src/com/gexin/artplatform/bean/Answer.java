package com.gexin.artplatform.bean;

import java.util.List;

public class Answer {

	private String _id;
	private String userId;
	private String problemId;
	private String avatarUrl;
	private List<AnswerContent> content;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public List<AnswerContent> getContent() {
		return content;
	}

	public void setContent(List<AnswerContent> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Answer [_id=" + _id + ", userId=" + userId + ", problemId="
				+ problemId + ", avatarUrl=" + avatarUrl + ", content="
				+ content + "]";
	}

}
