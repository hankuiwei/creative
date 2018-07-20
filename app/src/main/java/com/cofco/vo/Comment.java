
package com.cofco.vo;

import java.io.Serializable;

public class Comment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String photo;
	private String user_name;
	private String add_time;
	private String content;
	private String resive_name;
	private String resive_id;
	private String userid;
	private String creative_id;
	private String comment_id;
	private String comment_type;
	
	public String getComment_type() {
		return comment_type;
	}
	public void setComment_type(String comment_type) {
		this.comment_type = comment_type;
	}
	public String getResive_name() {
		return resive_name;
	}
	public void setResive_name(String resive_name) {
		this.resive_name = resive_name;
	}
	public String getResive_id() {
		return resive_id;
	}
	public void setResive_id(String resive_id) {
		this.resive_id = resive_id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCreative_id() {
		return creative_id;
	}
	public void setCreative_id(String creative_id) {
		this.creative_id = creative_id;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
}
