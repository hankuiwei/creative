package com.cofco.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.vo.UserInfo;
/**
 * 创建UserInfo对象
 * @author bin
 *
 */
public class UserInfoBuilder extends BaseBuilder<UserInfo> {

	@Override
	public UserInfo parseJSON(String paramString) throws JSONException {
		JSONObject obj = new JSONObject(paramString);
		String data = obj.getString("data");
		return JSON.parseObject(data,UserInfo.class);
	}

}
