package com.gexin.artplatform.bean;

import java.util.List;

public class Problem {

	private String _id;
	private int answerNum;
	private String avatarUrl;
	private String content;
	private String image;
	private String name;
	private long timestamp;
	private int viewNum;
	private int zan;
	private String userId;
	private List<Comment> commentList;
	private String askTo;
	private String tag;

	public Problem() {
	}

	public Problem(String _id, int answerNum, String avatarUrl, String content,
			String image, String name, long timestamp, int viewNum, int zan,
			String userId, List<Comment> commentList, String askTo) {
		super();
		this._id = _id;
		this.answerNum = answerNum;
		this.avatarUrl = avatarUrl;
		this.content = content;
		this.image = image;
		this.name = name;
		this.timestamp = timestamp;
		this.viewNum = viewNum;
		this.zan = zan;
		this.userId = userId;
		this.commentList = commentList;
		this.askTo = askTo;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

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

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public String getAskTo() {
		return askTo;
	}

	public void setAskTo(String askTo) {
		this.askTo = askTo;
	}
	

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Problem [_id=" + _id + ", answerNum=" + answerNum
				+ ", avatarUrl=" + avatarUrl + ", content=" + content
				+ ", image=" + image + ", name=" + name + ", timestamp="
				+ timestamp + ", viewNum=" + viewNum + ", zan=" + zan
				+ ", userId=" + userId + ", commentList=" + commentList
				+ ", askTo=" + askTo + ", tag=" + tag + "]";
	}

}
