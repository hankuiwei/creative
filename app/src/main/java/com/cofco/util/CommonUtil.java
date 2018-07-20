package com.cofco.util;

import java.io.File;

import com.cofco.R;

import android.app.AlertDialog;
/**
 *公共的弹出框
 */
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;
public class CommonUtil {

	public static void showInfoDialog(Context context, String message){
		showInfoDialog(context, message, "提示", "确定", null);
	}
	
	public static void showInfoDialog(Context context, String message, String titleStr, String positiveStr,
			DialogInterface.OnClickListener onClickListener){
		 AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		 localBuilder.setTitle(titleStr);
		 localBuilder.setMessage(message);
		 if(onClickListener == null)
			 onClickListener = new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
			 }};
		 localBuilder.setPositiveButton(positiveStr, onClickListener);
		 localBuilder.show();
	}
	public static void showInfoTost(Context context,String msg){
		Toast mToast =Toast.makeText(context, msg, 0);
//		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(0);
		mToast.show();
	}
	
	/**
	 * 取得存储路径如果没有SD卡，存本机
	 * @author wuchx
	 * @date 2014-1-21 下午2:31:17
	 * @return
	 */
	public static String getStotagePath(Context context){
		String path = "";
		
		if(isSDCardExist()){
			path = Environment.getExternalStorageDirectory().getPath();
		}else{
			path = context.getFilesDir().getPath();
		}
		
		return path;
	}
	
	/**
	 * 判断SD卡是否存在
	 * @author wuchx
	 * @return
	 */
	public static boolean isSDCardExist(){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			 return true; 
		}else{
			 return false; 
		}
	}
	
}
