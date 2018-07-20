package com.cofco.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.vo.CreateSquare;
import com.cofco.vo.CreativeItem;

public class CreativeSquareBuilder extends BaseBuilder<HashMap> {
	private HashMap mHashMap = new HashMap<String,String>();
	private ArrayList<CreativeItem> problemList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> solutionList= new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> newIdeaList= new ArrayList<CreativeItem>();
	@Override
	public HashMap parseJSON(String paramString) throws JSONException {
		JSONObject object = new JSONObject(paramString);
		JSONObject data= object.getJSONObject("data");
//		data.getString("1");
		boolean flag =data.has("1");
		if(data.has("1")){
			String pro = data.getString("1");
			problemList = (ArrayList<CreativeItem>) JSON.parseArray(pro, CreativeItem.class);
			mHashMap.put("problemList", problemList);
		}
		if(data.has("2")){
			String solu = data.getString("2");
			solutionList = (ArrayList<CreativeItem>) JSON.parseArray(solu, CreativeItem.class);
			mHashMap.put("solutionList", solutionList);
		}
		if(data.has("3")){
			String newIdea = data.getString("3");
			newIdeaList = (ArrayList<CreativeItem>) JSON.parseArray(newIdea, CreativeItem.class);
			mHashMap.put("newIdeaList", newIdeaList);
		}
		return mHashMap;
	}

}
