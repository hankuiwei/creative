package com.cofco.builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.QiInfoItem;

public class CreativeRankingBuilder extends BaseBuilder<HashMap> {
	private HashMap mHashMap = new HashMap<String,  ArrayList<CreativeItem>>();
	
	private ArrayList<QiInfoItem> qiInfoItems = new ArrayList<QiInfoItem>();
	private ArrayList<CreativeItem> votedAllCreateives = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> votedHeadCreateives = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> votedEndCreateives = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> votedOtherCreatives = new ArrayList<CreativeItem>();
	
	@Override
	public HashMap parseJSON(String paramString) throws JSONException {
		JSONObject object = new JSONObject(paramString);
		String data = object.getString("data");
		String qiinfo = object.getString("qiinfo");
		
		qiInfoItems = (ArrayList<QiInfoItem>) JSON.parseArray(qiinfo, QiInfoItem.class);
		votedAllCreateives = (ArrayList<CreativeItem>) JSON.parseArray(data, CreativeItem.class);
		
		int headsize = 3;
		int endsize = 10;
		int othersize = 0;
		
		if(votedAllCreateives.size() < 3){
			headsize = votedAllCreateives.size();
			endsize = 0;
		}else if(votedAllCreateives.size() >= 3 && votedAllCreateives.size() <= 10){
			endsize = votedAllCreateives.size();
		}else if(votedAllCreateives.size() > 10){
			othersize = votedAllCreateives.size();
		}
		
		for(int i = 0 ;i<headsize ; i ++ ){
			votedHeadCreateives.add(votedAllCreateives.get(i));
		}
		for(int i = 3 ;i<endsize ; i ++ ){
			votedEndCreateives.add(votedAllCreateives.get(i));
		}
		for(int i = 10 ;i<othersize ; i ++ ){
			votedOtherCreatives.add(votedAllCreateives.get(i));
		}
		
		mHashMap.put("qiinfo", qiInfoItems);
		mHashMap.put("votehead", votedHeadCreateives);
		mHashMap.put("voteend", votedEndCreateives);
		mHashMap.put("voteother", votedOtherCreatives);
		
		return mHashMap;
	}

}
