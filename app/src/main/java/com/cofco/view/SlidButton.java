package com.cofco.view;

/**
 * 
 */

import com.cofco.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;


/**
 * 滑动按钮效果
 * @author wuchx
 * @date 2013-11-16 下午1:43:49
 */
public class SlidButton extends Button implements OnTouchListener {

	private String tag = "SlidButton";

	private boolean onSlip = false;// 记录用户是否在滑动
	private float downY, nowY; // 按下时的Y坐标，当前的Y坐标

	private boolean isChgLsnOn = false;// 是否设置监听
	private OnChangedListener changedLis;

	private Bitmap bg_on, bg_off, slip_btn;

	private float initPosition;//游标初始位置
	private float middlePosition;//游标中间位置

	public SlidButton(Context context) {
		super(context);
		init();
	}

	public SlidButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// 载入图片资源
		bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.sild_bg_on);
		bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.sild_bg_off);
		slip_btn = BitmapFactory.decodeResource(getResources(), R.drawable.sild_btn);
		setOnTouchListener(this);

		initPosition = ((float) bg_off.getHeight() / 12) * 4;
		middlePosition = ((float) bg_off.getHeight() / 12) * 4 + (((float) bg_off.getHeight() / 12) * 8) / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float y = initPosition;
		if (nowY < middlePosition) // 滑动到前半段与后半段的背景不同,在此做判断
			canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
		else
			canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景

		if (onSlip) {// 是否是在滑动状态
			if (nowY >= bg_on.getHeight())// 是否划出指定范围,不能让游标跑到外头,必须做这个判断
				y = bg_on.getHeight() - slip_btn.getHeight() / 2;// 减去游标1/2的长度
			else
				y = nowY - slip_btn.getHeight() / 2;
		}

		if (y < initPosition)
			y = initPosition;
		if (y > bg_on.getHeight() - slip_btn.getHeight())
			y = bg_on.getHeight() - slip_btn.getHeight();

		canvas.drawBitmap(slip_btn, 0, y, paint);// 画出游标.
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {// 根据动作来执行代码

		case MotionEvent.ACTION_MOVE:// 滑动
			nowY = event.getY();
			break;
		case MotionEvent.ACTION_DOWN:// 按下
			onSlip = true;
			downY = event.getY();
			nowY = downY;
			break;
		case MotionEvent.ACTION_UP:// 松开
			onSlip = false;
			if (event.getY() >= middlePosition) 
			{
				nowY = initPosition;
				if(isChgLsnOn){
					changedLis.OnChanged(true);
				}
			}
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}

	public void SetOnChangedListener(OnChangedListener l) {// 设置监听器,当状态修改的时候
		isChgLsnOn = true;
		changedLis = l;
	}

	public interface OnChangedListener {
		abstract void OnChanged(boolean checkState);
	}

}
