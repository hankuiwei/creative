package com.cofco.builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.vo.Comment;
import com.cofco.vo.CreativeItem;

public class CreativeDetailBuilder extends BaseBuilder<HashMap>{
private CreativeItem creativeItem;
private ArrayList<Comment> comments;
private HashMap mHashMap = new HashMap();
	@Override
	public HashMap parseJSON(String paramString) throws JSONException {
		JSONObject object = new JSONObject(paramString);
		//JSONObject data = object.getJSONObject("data");
		String datastr = object.getString("data");
		/*if(data.has("details")){
			String details = data.getString("details");
			if(details.equals("")||details!=null){
			creativeItem = JSON.parseObject(details,CreativeItem.class);
			mHashMap.put("creativeItem", creativeItem);
				}
			}*/



			if(!datastr.equals("")||datastr!=null){
			creativeItem = JSON.parseObject(datastr,CreativeItem.class);
			mHashMap.put("creativeItem", creativeItem);
				}



		/*if(data.has("comment")){
			String comment = data.getString("comment");
			if(!comment.equals("")&&comment!=null){
				comments = (ArrayList<Comment>) JSON.parseArray(comment,Comment.class);
				mHashMap.put("comments", comments);
			}
		}*/
		return mHashMap;
	}

}
