package com.cofco.builder;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.cofco.builder.BaseBuilder;
import com.cofco.vo.CreativeItem;

public class CreativeListBuilder extends BaseBuilder<List<CreativeItem>> {

	@Override
	public List<CreativeItem> parseJSON(String paramString)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(paramString);
		//String str = jsonObject.getString("data");
		JSONObject dataJson = jsonObject.getJSONObject("data");
		return JSONArray.parseArray(dataJson.getString("createLBeans"), CreativeItem.class);
	}

}
