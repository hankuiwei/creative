package com.cofco;

import android.view.View;
import android.widget.ImageView;
/**
 * 关于页面
 * @author bin
 *
 */
public class AboutActivity extends BaseActivity{
	private ImageView mBackButton;
	

	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_about_us);
	}

	@Override
	protected void processLogic() {
		
	}
	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;

		default:
			break;
		}
	}
}
