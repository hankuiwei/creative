package com.cofco.view;

import com.cofco.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PathMenuView extends FrameLayout {
	private static final int LEFT_TOP = 0;

	private static final int RIGHT_TOP = 1;

	private static final int RIGHT_BOTTOM = 2;

	private static final int LEFT_BOTTOM = 3;
	
	/** 
     */  
    private int position = 3;  
      
    /** 
     */  
    private ImageView mHome;  
      
    /** 
     */  
    private Context mContext;  
      
      
    /** 
     */  
    private int mWIDTH = 0;  
      
    /** 
     */  
    private int mHEIGHT = 0;  
      
    /** 
     */  
    private float mDensity;  
      
    /** 
     */  
    private boolean bMenuShow;  
      
    private static int  xOffset     = 15;  
    private static int  yOffset     = -13; 
    
    /** 
     */  
    private int[] menuResIds = {R.drawable.h_icon1,R.drawable.h_icon2,  
            R.drawable.h_icon2,R.drawable.h_icon3,R.drawable.h_icon4,R.drawable.h_icon5};  
	public PathMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PathMenuView(Context context) {
		super(context);
	}

	public PathMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		RectF rectF = new RectF(50,20,100,80);
		paint.setColor(Color.BLACK);
		canvas.drawArc(rectF, 90, 270, true, paint);
		super.onDraw(canvas);
	}

	private void setupViews(){
		 mContext = getContext();  
	        mHEIGHT = mContext.getResources().getDisplayMetrics().heightPixels;  
	        mWIDTH = mContext.getResources().getDisplayMetrics().widthPixels;  
	        mDensity = mContext.getResources().getDisplayMetrics().density;  
	        xOffset = (int) (10.667 * mDensity);  
	        yOffset = (int) (8.667 * mDensity);  
	          
	        mHome = new ImageView(mContext);  
	          
	        mHome.setImageResource(R.drawable.bottom_main_selector);  
	        mHome.setOnClickListener(listener);  
	        addView(mHome); 
	        LayoutParams mHomeparams = (FrameLayout.LayoutParams)mHome.getLayoutParams();  
	        mHomeparams.width = LayoutParams.WRAP_CONTENT;  
	        mHomeparams.height = LayoutParams.WRAP_CONTENT;  
	        switch (position) {  
	        case LEFT_TOP:  
	            mHomeparams.gravity = Gravity.LEFT | Gravity.TOP;  
	            for (int i = 0; i < menuResIds.length; i++) {  
	  
	                int width_padding = mWIDTH / ((menuResIds.length - 1) * 2);  
	                int height_padding = mHEIGHT / ((menuResIds.length - 1) * 2);  
	  
	                ImageView imageView = new ImageView(mContext);  
	                imageView.setImageResource(menuResIds[i]);  
	                addView(imageView);  
	                LayoutParams params = (FrameLayout.LayoutParams) imageView  
	                        .getLayoutParams();  
	                params.width = LayoutParams.WRAP_CONTENT;  
	                params.height = LayoutParams.WRAP_CONTENT;  
	                params.leftMargin = mWIDTH / 2  
	                        - ((menuResIds.length - i - 1) * width_padding);  
	                params.topMargin = mHEIGHT / 2 - i * height_padding;  
	                params.gravity = Gravity.LEFT | Gravity.TOP;  
	                imageView.setLayoutParams(params);  
	  
	            }  
	              
	            break;  
	        case RIGHT_TOP:  
	            mHomeparams.gravity = Gravity.RIGHT | Gravity.TOP;  
	            for (int i = 0; i < menuResIds.length; i++) {  
	  
	                int width_padding = mWIDTH / ((menuResIds.length - 1) * 2);  
	                int height_padding = mHEIGHT / ((menuResIds.length - 1) * 2);  
	  
	                ImageView imageView = new ImageView(mContext);  
	                imageView.setImageResource(menuResIds[i]);  
	                addView(imageView);  
	                LayoutParams params = (FrameLayout.LayoutParams) imageView  
	                        .getLayoutParams();  
	                params.width = LayoutParams.WRAP_CONTENT;  
	                params.height = LayoutParams.WRAP_CONTENT;  
	                params.rightMargin = mWIDTH / 2  
	                        - ((menuResIds.length - i - 1) * width_padding);  
	                params.topMargin = mHEIGHT / 2 - i * height_padding;  
	                params.gravity = Gravity.RIGHT | Gravity.TOP;  
	                imageView.setLayoutParams(params);  
	  
	            }  
	            break;  
	        case RIGHT_BOTTOM:  
	            mHomeparams.gravity = Gravity.RIGHT | Gravity.BOTTOM;  
	            for (int i = 0; i < menuResIds.length; i++) {  
	  
	                int width_padding = mWIDTH / ((menuResIds.length - 1) * 2);  
	                int height_padding = mHEIGHT / ((menuResIds.length - 1) * 2);  
	  
	                ImageView imageView = new ImageView(mContext);  
	                imageView.setImageResource(menuResIds[i]);  
	                addView(imageView);  
	                LayoutParams params = (FrameLayout.LayoutParams) imageView  
	                        .getLayoutParams();  
	                params.width = LayoutParams.WRAP_CONTENT;  
	                params.height = LayoutParams.WRAP_CONTENT;  
	                params.rightMargin = mWIDTH / 2  
	                        - ((menuResIds.length - i - 1) * width_padding);  
	                params.bottomMargin = mHEIGHT / 2 - i * height_padding;  
	                params.gravity = Gravity.RIGHT | Gravity.BOTTOM;  
	                imageView.setLayoutParams(params);  
	  
	            }  
	            break;  
	        case LEFT_BOTTOM:  
	            mHomeparams.gravity = Gravity.LEFT | Gravity.BOTTOM;  
	            for(int i = 0; i < menuResIds.length; i++){  
	                  
	                int width_padding = mWIDTH / ((menuResIds.length - 1) * 2);  
	                int height_padding = mHEIGHT / ((menuResIds.length -1) * 2);  
	                  
	                ImageView imageView = new ImageView(mContext);  
	                imageView.setImageResource(menuResIds[i]);  
	                addView(imageView);  
	                LayoutParams params = (FrameLayout.LayoutParams)imageView.getLayoutParams();  
	                params.width = LayoutParams.WRAP_CONTENT;  
	                params.height = LayoutParams.WRAP_CONTENT;            
	                params.leftMargin = mWIDTH / 2 - ((menuResIds.length - i - 1) * width_padding);  
	                params.bottomMargin = mHEIGHT / 2 - i * height_padding;  
	                params.gravity = Gravity.LEFT | Gravity.BOTTOM;  
	                imageView.setLayoutParams(params);                        
	            }  
	            break;  
	        default:  
	                break;  
	        }     
	        mHome.setLayoutParams(mHomeparams);    
	}
	 private OnClickListener listener = new OnClickListener() {  
			@Override
			public void onClick(View v) {
				  if (!bMenuShow) {  
		                startAnimationIn(PathMenuView.this, 300);  
		            } else {  
		                startAnimationOut(PathMenuView.this, 300);  
		            }  
		            bMenuShow = !bMenuShow; 
			}

	    }; 
	    
	    /** 
	     * �˵����ض���. 
	     *  
	     * @param group 
	     * @param duration 
	     */  
	    private void startAnimationIn(ViewGroup group, int duration) {  
	        for (int i = 1; i < group.getChildCount(); i++) {  
	            ImageView imageview = (ImageView) group.getChildAt(i);  
	            imageview.setVisibility(0);  
	            MarginLayoutParams mlp = (MarginLayoutParams) imageview  
	                    .getLayoutParams();  
	              
	              
	            Animation animation = null;  
	              
	              
	            switch (position) {  
	            case LEFT_TOP:  
	                animation = new TranslateAnimation(0F,-mlp.leftMargin+xOffset,0F,-mlp.topMargin + yOffset);  
	                break;  
	            case RIGHT_TOP:  
	                animation = new TranslateAnimation(mlp.rightMargin - xOffset,0F,-mlp.topMargin + yOffset,0F);  
	                break;            
	            case LEFT_BOTTOM:  
	                animation = new TranslateAnimation(0F, -mlp.leftMargin+ xOffset, 0F, -yOffset + mlp.bottomMargin);  
	                break;  
	                  
	            case RIGHT_BOTTOM:  
	                animation = new TranslateAnimation(mlp.rightMargin-xOffset,0F,-yOffset + mlp.bottomMargin, 0F);  
	                break;  
	            default:  
	                break;  
	            }  
	  
	            animation.setFillAfter(true);  
	            animation.setDuration(duration);  
	            animation.setStartOffset((i * 100) / (-1 + group.getChildCount()));  
	            animation.setInterpolator(new OvershootInterpolator(2F));  
	            imageview.startAnimation(animation);  
	  
	        }  
	    }  
	      
	    /** 
	     * �˵���ʾ����. 
	     *  
	     * @param group 
	     * @param duration 
	     */  
	    private void startAnimationOut(ViewGroup group,int duration){  
	        for (int i = 1; i < group.getChildCount(); i++) {  
	            final ImageView imageview = (ImageView) group  
	                    .getChildAt(i);  
	            MarginLayoutParams mlp = (MarginLayoutParams) imageview.getLayoutParams();  
	              
	            Animation animation = null;  
	              
	            switch (position) {  
	            case LEFT_TOP:  
	                animation = new TranslateAnimation(-mlp.leftMargin+xOffset,0F,-mlp.topMargin + yOffset,0F);  
	                break;  
	            case RIGHT_TOP:  
	                animation = new TranslateAnimation(0F,mlp.rightMargin - xOffset,0F,-mlp.topMargin + yOffset);  
	                break;  
	  
	            case LEFT_BOTTOM:  
	                animation = new TranslateAnimation(-mlp.leftMargin+xOffset,0F, -yOffset + mlp.bottomMargin,0F);  
	                break;  
	  
	            case RIGHT_BOTTOM:  
	                animation = new TranslateAnimation(0F,mlp.rightMargin-xOffset, 0F,-yOffset + mlp.bottomMargin);  
	                break;  
	            default:  
	                break;  
	            }  
	              
	            animation.setFillAfter(true);animation.setDuration(duration);  
	            animation.setStartOffset(((group.getChildCount()-i) * 100)  
	                    / (-1 + group.getChildCount()));  
	            animation.setInterpolator(new AnticipateInterpolator(2F));  
	            imageview.startAnimation(animation);  
	        }  
	    }  
	  

}
