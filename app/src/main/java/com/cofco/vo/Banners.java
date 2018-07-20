package com.cofco.vo;
import java.io.Serializable;


public class Banners implements Serializable{
	private Banner sml;
	private Banner big;
	public Banner getSml() {
		return sml;
	}
	public void setSml(Banner sml) {
		this.sml = sml;
	}
	public Banner getBig() {
		return big;
	}
	public void setBig(Banner big) {
		this.big = big;
	}
	
}
