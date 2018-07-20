package com.cofco.builder;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.Message;

public class MessageBuilder extends BaseBuilder<ArrayList<Message>>{

	@Override
	public ArrayList<Message> parseJSON(String paramString) throws JSONException {
		JSONObject object = new JSONObject(paramString);
		//String data = null;
		JSONObject dataJson = null;
		if(object.has("data")){
		   //data = object.getString("data");
			dataJson = object.getJSONObject("data");
		   if(dataJson!=null){
			   return (ArrayList<Message>) JSON.parseArray(dataJson.getString("messageBeans"),Message.class);
		   }
		}
		return null;
	}

}
