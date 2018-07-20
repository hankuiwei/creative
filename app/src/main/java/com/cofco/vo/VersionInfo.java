package com.cofco.vo;

import java.io.Serializable;

public class VersionInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String andriod_version_number;
	private String andriod_url;
	public String getAndriod_version_number() {
		return andriod_version_number;
	}
	public void setAndriod_version_number(String andriod_version_number) {
		this.andriod_version_number = andriod_version_number;
	}
	public String getAndriod_url() {
		return andriod_url;
	}
	public void setAndriod_url(String andriod_url) {
		this.andriod_url = andriod_url;
	}
	
}
