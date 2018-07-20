package com.cofco;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.util.Constant;
import com.cofco.util.Util;
import com.cofco.view.ToastTool;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CenterFeedBackActivity extends BaseActivity{
	private ImageView mBackButton;
	private Button mSubmitButton;
	private EditText mFeedBackContent;
	private HashMap mHashMap = new HashMap();
	private SharedPreferences sp;
	private String uid;
	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mSubmitButton = (Button) this.findViewById(R.id.submit);
		mFeedBackContent = (EditText) this.findViewById(R.id.center_feedback_content);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_feedback);
//		ToastTool.toast(CenterFeedBackActivity.this,  "反饋成功!", "s");
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		uid=sp.getString("uid", "");
	}

	@Override
	protected void processLogic() {
		
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
		mSubmitButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
		   finish();
		   overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		   break;
		case R.id.submit:
			String feedbackContent = mFeedBackContent.getText().toString().trim();
			if(feedbackContent.equals("")||feedbackContent==null){
				Toast mToast =Toast.makeText(CenterFeedBackActivity.this, "内容不能为空!", 0);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.setDuration(0);
				mToast.show();
				return;
			}
//			feedbackContent = URLEncoder.encode(feedbackContent);
			mHashMap.put("uid", uid);
			mHashMap.put("content", feedbackContent);
			RequestVo vo = new RequestVo();
			vo.mContext = CenterFeedBackActivity.this;
			vo.mRequestUrl = getString(R.string.feedback);
			vo.mRequestDataMap = mHashMap;
			//提交数据
			getDataFromServer(vo, new DataCallback<String>() {
				@Override
				public void processData(String paramObject, boolean paramBoolean) {
					
						try {
							JSONObject object  = new JSONObject(paramObject);
							String status =object.getString("status");
							String msg = object.getString("msg");
							if(status.equals("200")){
								ToastTool.toast(CenterFeedBackActivity.this, msg, "s");
								mFeedBackContent.setText("");
								finish();
								return;
							}else{
								Util.showToast(CenterFeedBackActivity.this, msg);
								return;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							return;
						}
				}
			}, Constant.DO_POST, "正在提交...");
			break;
		}
	}
}
