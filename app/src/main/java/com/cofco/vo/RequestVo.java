package com.cofco.vo;

import java.io.File;
import java.util.HashMap;

import com.cofco.builder.BaseBuilder;


import android.content.Context;
/**
 * 请求参数的封装
 * @author bin
 *
 */
public class RequestVo {
	public String mRequestUrl;
	public Context mContext;
	public HashMap<String, String> mRequestDataMap;
	public BaseBuilder<?> mJsonBuilder;
	public File mFile;
	public File[] mFiles;
	public RequestVo() {
	}

	public RequestVo(String requestUrl, Context context, HashMap<String, String> requestDataMap, BaseBuilder<?> jsonBuilder) {
		super();
		this.mRequestUrl = requestUrl;
		this.mContext = context;
		this.mRequestDataMap = requestDataMap;
		this.mJsonBuilder = jsonBuilder;
	}
	public RequestVo(String requestUrl, Context context, HashMap<String, String> requestDataMap) {
		super();
		this.mRequestUrl = requestUrl;
		this.mContext = context;
		this.mRequestDataMap = requestDataMap;
	}
	public RequestVo(String requestUrl, Context context, HashMap<String, String> requestDataMap, File file) {
		super();
		this.mRequestUrl = requestUrl;
		this.mContext = context;
		this.mRequestDataMap = requestDataMap;
		this.mFile = file;
	}
	public RequestVo(String requestUrl, Context context, HashMap<String, String> requestDataMap, File[] files) {
		super();
		this.mRequestUrl = requestUrl;
		this.mContext = context;
		this.mRequestDataMap = requestDataMap;
		this.mFiles = files;
	}
}
