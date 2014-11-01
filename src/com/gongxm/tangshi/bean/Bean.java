package com.gongxm.tangshi.bean;

import java.io.Serializable;

public class Bean implements Serializable{
	private String title;
	private String author;
	private String desc;
	private String textUrl;
	public Bean(String title, String author, String desc, String textUrl) {
		super();
		this.title = title;
		this.author = author;
		this.desc = desc;
		this.textUrl = textUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTextUrl() {
		return textUrl;
	}
	public void setTextUrl(String textUrl) {
		this.textUrl = textUrl;
	}
	
}
