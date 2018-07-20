package com.cofco.vo;

import java.io.Serializable;

import com.cofco.R;
import com.cofco.util.Constant;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName="creative_item")
public class CreativeItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 @DatabaseField(generatedId = true)
	 private int id;
	@DatabaseField
	private int creative_id;
	@DatabaseField
	private String creative_name;
	@DatabaseField
	private String creative_description;
	@DatabaseField
	private int sort_id;
	@DatabaseField
	private String creative_type;
	@DatabaseField
	private String collect_num;
	@DatabaseField
	private String praise_num;
	@DatabaseField
	private int collect_id;
	@DatabaseField
	private String comment_num;
	@DatabaseField
	private String create_time;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private String[] photo;
	@DatabaseField
	private int vote_status;
	@DatabaseField
	private boolean collect_status;
	@DatabaseField
	private boolean praise_status;
	@DatabaseField
	private boolean comment_status;
	@DatabaseField
	private int vote_num;
	@DatabaseField
	private int status;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getVote_num() {
		return vote_num;
	}
	public void setVote_num(int vote_num) {
		this.vote_num = vote_num;
	}
	public int getCollect_id() {
		return collect_id;
	}
	public void setCollect_id(int collect_id) {
		this.collect_id = collect_id;
	}
	public boolean isComment_status() {
		return comment_status;
	}
	
	public int getVote_status() {
		return vote_status;
	}
	public void setVote_status(int vote_status) {
		this.vote_status = vote_status;
	}
	public void setComment_status(boolean comment_status) {
		this.comment_status = comment_status;
	}
	public boolean isCollect_status() {
		return collect_status;
	}
	public void setCollect_status(boolean collect_status) {
		this.collect_status = collect_status;
	}
	public boolean isPraise_status() {
		return praise_status;
	}
	public void setPraise_status(boolean praise_status) {
		this.praise_status = praise_status;
	}
	public String getCreative_type() {
		return creative_type;
	}
	public void setCreative_type(String creative_type) {
		this.creative_type = creative_type;
	}

	public String[] getPhoto() {
		return this.photo;
	}
	public void setPhoto(String[] photo) {
		this.photo = photo;
	}
	public String getCollect_num() {
		return collect_num;
	}

	public void setCollect_num(String collect_num) {
		this.collect_num = collect_num;
	}

	public String getPraise_num() {
		return praise_num;
	}

	public void setPraise_num(String praise_num) {
		this.praise_num = praise_num;
	}

	public String getComment_num() {
		return comment_num;
	}

	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}

	public int getSort_id() {
		return sort_id;
	}

	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}

	

	public int getCreative_id() {
		return creative_id;
	}

	public void setCreative_id(int creative_id) {
		this.creative_id = creative_id;
	}

	public String getCreative_name() {
		
		return creative_name;
	}

	public void setCreative_name(String creative_name) {
		this.creative_name = creative_name;
	}

	public String getCreative_description() {
		return creative_description;
	}

	public void setCreative_description(String creative_description) {
		this.creative_description = creative_description;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
