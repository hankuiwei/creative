package com.cofco.vo;

import java.io.Serializable;

import com.cofco.R;
import com.cofco.util.Constant;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public class QiInfoItem implements Serializable {

	private int id;
	private String dangqi_name;
	private String start_time;
	private String end_time;
	private String person_votenum;
	private String update_time;
	private int qi;
	
	public QiInfoItem(){
	}
	
	public QiInfoItem(int id, String dangqi_name, String start_time, String end_time, String person_votenum, String update_time, int qi) {
		super();
		this.id = id;
		this.dangqi_name = dangqi_name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.person_votenum = person_votenum;
		this.update_time = update_time;
		this.qi = qi;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDangqi_name() {
		return dangqi_name;
	}

	public void setDangqi_name(String dangqi_name) {
		this.dangqi_name = dangqi_name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getPerson_votenum() {
		return person_votenum;
	}

	public void setPerson_votenum(String person_votenum) {
		this.person_votenum = person_votenum;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public int getQi() {
		return qi;
	}

	public void setQi(int qi) {
		this.qi = qi;
	}
	
}
