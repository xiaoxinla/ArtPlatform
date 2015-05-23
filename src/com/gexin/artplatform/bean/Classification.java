package com.gexin.artplatform.bean;

public class Classification {
	private String href;
	private String title;
	private String icon;
	private String ID;
	private String superior;

	public Classification() {

	};

	public Classification(String href, String title, String icon, String iD,
			String superior) {
		super();
		this.href = href;
		this.title = title;
		this.icon = icon;
		ID = iD;
		this.superior = superior;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	@Override
	public String toString() {
		return "Classification [href=" + href + ", title=" + title + ", icon="
				+ icon + ", ID=" + ID + ", superior=" + superior + "]";
	}

}
