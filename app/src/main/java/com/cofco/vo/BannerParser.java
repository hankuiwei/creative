package com.cofco.vo;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.builder.BaseBuilder;

public class BannerParser extends BaseBuilder<ArrayList<Banners>>{

	@Override
	public ArrayList<Banners> parseJSON(String paramString)
			throws JSONException {
		JSONObject object = new JSONObject(paramString);
		if(object.has("data")){
			String data = object.getString("data");
			return (ArrayList<Banners>) JSON.parseArray(data, Banners.class);
		}
		return null;
	}

}
