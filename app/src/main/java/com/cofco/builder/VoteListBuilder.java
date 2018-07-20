package com.cofco.builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.vo.CreativeItem;

public class VoteListBuilder extends BaseBuilder<HashMap> {
	private HashMap mHashMap = new HashMap<String, ArrayList<CreativeItem>>();
	private ArrayList<CreativeItem> newVoteList;
	private ArrayList<CreativeItem> voteHistoryList;
	private String num;
	private String creativeNum;
	private String voteNum="0";
	@Override
	public HashMap parseJSON(String paramString) throws JSONException {
		JSONObject object = new JSONObject(paramString);
		if (object.has("num")) {
			num = object.getString("num");
			mHashMap.put("num", num);
		}
		if(object.has("creative_num")){
			creativeNum = object.getString("creative_num");
			mHashMap.put("creative_num", creativeNum);
		}
		if(object.has("vote_num")){
			voteNum = object.getString("vote_num");
			mHashMap.put("vote_num", voteNum);
		}
		if (object.has("data")) {
			String data = object.getString("data");
			newVoteList = (ArrayList<CreativeItem>) JSON.parseArray(data,
					CreativeItem.class);
			mHashMap.put("vote", newVoteList);
		}
		// String voteHistory = data.getString("success");
		// if(!voteHistory.equals("")||voteHistory!=null){
		// voteHistoryList = (ArrayList<CreativeItem>)
		// JSON.parseArray(voteHistory,CreativeItem.class);
		// mHashMap.put("voteHistoryList", voteHistoryList);
		// }
		return mHashMap;
	}

}
