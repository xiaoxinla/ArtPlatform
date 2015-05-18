package com.gexin.artplatform.bean;

public class Classification {
	private String href;
	private String title;
	private String icon;
	private String ID;

	public Classification() {

	}

	public Classification(String herf, String title, String img, String superior) {
		this.href = herf;
		this.icon = img;
		this.title = title;
		this.ID = superior;
	}

	public String getHref() {
		return href;
	}

	public String getTitle() {
		return title;
	}

	public String getIcon() {
		return icon;
	}

	public String getID() {
		return ID;
	}

	public void setHref(String herf) {
		this.href = herf;
	}

	public void setIcon(String img) {
		this.icon = img;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setID(String superior) {
		this.ID = superior;
	}

	@Override
	public String toString() {
		return "Classification [href=" + href + ", title=" + title + ", icon="
				+ icon + ", ID=" + ID + "]";
	}

}
