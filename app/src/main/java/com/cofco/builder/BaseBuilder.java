package com.cofco.builder;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 解析json数据
 * @author bin
 *
 * @param <T>
 */
public abstract class BaseBuilder<T> {
private HashMap mHashMap = new HashMap<String, String>();
	public abstract T parseJSON(String paramString) throws JSONException;

	/**检查返回的状态码
	 * @param res
	 * @throws JSONException
	 */
	public HashMap checkResponse(String paramString) throws JSONException {
		if (paramString == null) {
			return null;
		} else {
			JSONObject jsonObject = new JSONObject(paramString);
			String status = jsonObject.getString("status");
			String msg = jsonObject.getString("msg");
			mHashMap.put("status", status);
			mHashMap.put("msg", msg);
			return mHashMap;
		}
	}
}
