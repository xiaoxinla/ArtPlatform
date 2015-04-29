package com.gexin.artplatform.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Problem {

	private String id;
	private int answerNum;
	private String avatarUrl;
	private String content;
	private String image;
	private String name;
	private long timestamp;
	private String userid;
	private int viewNum;
	private int zan;

	public Problem() {
	}

	public Problem(String id, int answerNum, String avatarUrl, String content,
			String image, String name, long timestamp, String userid,
			int viewNum, int zan) {
		super();
		this.id = id;
		this.answerNum = answerNum;
		this.avatarUrl = avatarUrl;
		this.content = content;
		this.image = image;
		this.name = name;
		this.timestamp = timestamp;
		this.userid = userid;
		this.viewNum = viewNum;
		this.zan = zan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	@Override
	public String toString() {
		return "Problem [id=" + id + ", answerNum=" + answerNum
				+ ", avatarUrl=" + avatarUrl + ", content=" + content
				+ ", image=" + image + ", name=" + name + ", timestamp="
				+ timestamp + ", userid=" + userid + ", viewNum=" + viewNum
				+ ", zan=" + zan + "]";
	}
	
	public static Problem analyzeJson(JSONObject jsonObject){
		Problem problem = null;
		try {
			String id = jsonObject.getString("_id");
			String avatarUrl = jsonObject.getString("avatarUrl");
			String content = jsonObject.getString("content");
			String image = jsonObject.getString("image");
			String name = jsonObject.getString("name");
			String user_id = jsonObject.getString("userId");
			int answerNum = jsonObject.getInt("answerNum");
			long timestamp = jsonObject.getLong("timestamp");
			int viewNum = jsonObject.getInt("viewNum");
			int zan = jsonObject.getInt("zan");
			problem = new Problem(id, answerNum, avatarUrl,
					content, image, name, timestamp, user_id, viewNum,
					zan);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return problem;
	}

}
