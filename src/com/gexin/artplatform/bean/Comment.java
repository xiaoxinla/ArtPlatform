package com.gexin.artplatform.bean;

public class Comment {

	private String _id;
	private String problemId;
	private String content;
	private String replyTo;
	private String fromUser;
	private long timestamp;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Comment [_id=" + _id + ", problemId=" + problemId
				+ ", content=" + content + ", replyTo=" + replyTo
				+ ", fromUser=" + fromUser + ", timestamp=" + timestamp + "]";
	}

}
