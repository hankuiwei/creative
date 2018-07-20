package com.cofco.util;

import android.graphics.Bitmap;

/**
 * @author bin
 *
 */
public class Constant
{	
	public final static int FAILED = 1;
	public final static int SUCCESS = 1;
	public final static int NET_FAILED = 2;
	public final static int TIME_OUT = 3;
	//请求的类型，doPost,doGet,doPost(url,File);
	public final static int DO_GET = 1;
	public final static int DO_POST = 2;
	public final static int DO_POST_FILE=3;
	public static final int Do_POST_FILES = 4;
	//创意的类型
	public final static int PRODUCT=0;//产品类
	public final static int TECHNOLOGY=1;//技术类
	public final static int PACK=2;//包装类
	public final static int MARKTING=3;//营销类
	public final static int OTHER=4;//其他
	public final static int ALL=5;//其他
	public final static int VOTE_HISTORY=6;//历史投票
	public final static int VOTE_NEW=7;//最新投票
	public static boolean UPDATE_LIST = false;
	//public static final String IMAGE_BASE_URL = "http://idea.cofco.com";
	public static final String IMAGE_BASE_URL = "http://idea.cofco.com";
	public static int MESSAGE_NUM=0;
	public static Bitmap userThumb=null;
	//对话框种类
	public static final int FAVORITE = 21;
	public static final int MESSAGE = 22;
	public static  String PAGE_TAG ="newVote";
    public static boolean isVoted = false;
    public static final String APP_SHARE_URL="http://idea.cofco.com/andriodurl/creative.apk";
}	