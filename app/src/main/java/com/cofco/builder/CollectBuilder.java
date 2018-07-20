package com.cofco.builder;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.builder.BaseBuilder;
import com.cofco.vo.CreativeItem;

public class CollectBuilder extends BaseBuilder<ArrayList<CreativeItem>> {
	private ArrayList<CreativeItem> creativeList;
	@Override
	public ArrayList<CreativeItem> parseJSON(String paramString) throws JSONException {
		JSONObject object = new JSONObject(paramString);
		String data = object.getString("data");
		if(!data.equals("")&&data!=null){
			 creativeList = (ArrayList<CreativeItem>) JSON.parseArray(data, CreativeItem.class);
			 return creativeList;
		}
		return null;
	}

}
