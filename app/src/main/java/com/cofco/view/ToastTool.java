package com.cofco.view;

import com.cofco.R;

import android.content.Context;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastTool {  
	      
	    /** 
	     *  
	     * @param context 上下文对象 
	     * @param msg 要显示的信息 
	     * @param timeTag 时间参数 若是“s”表示短时间显示  
	     *                           若是“l”（小写L）表示长时间显示 
	     */  
	    public static void toast(Context context, String msg, String timeTag){  
	        int time = Toast.LENGTH_SHORT;  
	        if(timeTag == null || "l".equals(timeTag)){  
	            time = Toast.LENGTH_LONG;  
	        }  
	          
	        Toast toast = Toast.makeText(context, null, time);  
	        LinearLayout layout = (LinearLayout)toast.getView(); 
	        layout.setOrientation(LinearLayout.VERTICAL);
	        /*layout.setLayoutParams(new WindowManager.LayoutParams(10000, 
	                android.view.WindowManager.LayoutParams.WRAP_CONTENT,  
	                WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
	                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 
	                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,  
	                PixelFormat.TRANSLUCENT));*/  
	        layout.setBackgroundResource(R.drawable.toast_frame);  
	        layout.setOrientation(LinearLayout.VERTICAL);  
	        layout.setGravity(Gravity.CENTER);  
	        Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
	        ImageView iv = new ImageView(context);
	        iv.setLayoutParams(new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        iv.setBackgroundResource(R.drawable.confirm_dialog_successful_icon);
//	        iv.setPadding(0, 0, 0, 10);
//	        iv.setAnimation(anim);
//	        iv.startAnimation(anim);
	        layout.addView(iv,0);
//	        TextView tv = new TextView(context);  
//	        tv.setLayoutParams(new  LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));     
////	        tv.setGravity(Gravity.BOTTOM);  
//	        tv.setTextColor(Color.parseColor("#ffffffff"));  
//	        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);  
//	        tv.setPadding(0, 20,0, 0);  
//	        tv.setText(msg);  
//	        tv.startAnimation(anim);
//	        layout.addView(tv,1);  
	        layout.setAnimation(anim);
	        layout.startAnimation(anim);
	        toast.setGravity(Gravity.CENTER, 0, -50);
	        toast.show();  
	    }  
}