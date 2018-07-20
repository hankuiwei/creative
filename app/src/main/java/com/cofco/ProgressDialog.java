package com.cofco;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ProgressDialog extends Activity{
	private Dialog mDialog;
	private ImageView mImageView;
	private Animation mAnimation;
	private LinearLayout mLinearLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialogview, null);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.dialog_view);
		mImageView = (ImageView) view.findViewById(R.id.circle);
		// 加载动画
		mAnimation = AnimationUtils.loadAnimation(this,
				R.anim.animation);
		// 使用ImageView显示动画
		mImageView.startAnimation(mAnimation);
		mDialog = new Dialog(ProgressDialog.this,
				R.style.FullHeightDialog);
		mDialog.setCancelable(true);
		mDialog.show();
		mDialog.setContentView(mLinearLayout, new LinearLayout.LayoutParams(180,
				LinearLayout.LayoutParams.WRAP_CONTENT));

	}
}
