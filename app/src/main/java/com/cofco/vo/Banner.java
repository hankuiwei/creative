package com.cofco.vo;
import java.io.Serializable;


public class Banner implements Serializable {
	private int obj_id;
	private String img;
	private String imgw;
	private String imageh;
	public int getObj_id() {
		return obj_id;
	}
	public void setObj_id(int obj_id) {
		this.obj_id = obj_id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImgw() {
		return imgw;
	}
	public void setImgw(String imgw) {
		this.imgw = imgw;
	}
	public String getImageh() {
		return imageh;
	}
	public void setImageh(String imageh) {
		this.imageh = imageh;
	}
	
}
