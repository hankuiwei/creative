package com.cofco;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.BaseActivity.DataCallback;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.vo.Comment;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplayActivity extends BaseActivity{
	private ImageView mBackButton;
	private ImageView mSubmitButton;
	private EditText mReplayContent;
	private SharedPreferences sp;
	private HashMap mHashMap = new HashMap<String, String>();
	private CreativeItem creativeItem;
	private Comment comment;
	private TextView mReplayTitle;

	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mSubmitButton = (ImageView) this.findViewById(R.id.submit);
		mReplayContent = (EditText) this
				.findViewById(R.id.center_comment_content);
		mReplayTitle = (TextView) findViewById(R.id.feedback_title);
		mReplayContent.setHint("回复:");
		mReplayTitle.setText("回复");
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_comment);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		String user_code = sp.getString("user_code", "");
//		creativeItem = (CreativeItem) getIntent().getExtras().get(
//				"creative_item");
		comment = (Comment) getIntent().getExtras().get("comment");
		String sendUserId = uid;//发送者信息
		String reciveUserId = comment.getUserid();
		String creativeId = comment.getCreative_id();
		mHashMap.put("send_uid", uid);
		mHashMap.put("receive_uid", reciveUserId);
		mHashMap.put("creative_id", creativeId);
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
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			break;
		case R.id.submit:
			if (!NetUtil.hasNetwork(ReplayActivity.this)) {
				CommonUtil.showInfoTost(ReplayActivity.this,
						getString(R.string.net_errors));
				return;
			}
			String content = mReplayContent.getText().toString().trim();
			if (!content.equals("")) {
				mHashMap.put("content", content);
				RequestVo vo = new RequestVo();
				vo.mContext = ReplayActivity.this;
				vo.mRequestUrl = getString(R.string.comment_reply);
				vo.mRequestDataMap = mHashMap;
				super.getDataFromServer(vo, new DataCallback<String>() {
					@Override
					public void processData(String paramObject ,
							boolean paramBoolean) {
						if (paramObject == null) {
							Util.showToast(ReplayActivity.this, "回复失败!");
						} else {
							try {
								JSONObject object = new JSONObject(paramObject);
								String status = object.getString("status");
								String msg = object.getString("msg");
								if (status.equals("200")) {
									Util.showToast(ReplayActivity.this, msg);
									Intent intent = new Intent();
									ReplayActivity.this.setResult(RESULT_OK);
									finish();
									overridePendingTransition(
											R.anim.slide_left_in,
											R.anim.slide_right_out);
								} else if (status.equals("0")) {
									Intent intent = new Intent();
									ReplayActivity.this.setResult(RESULT_OK);
									Util.showToast(ReplayActivity.this, msg);
									finish();
									overridePendingTransition(
											R.anim.slide_left_in,
											R.anim.slide_right_out);
								} else {
									Intent intent = new Intent();
									ReplayActivity.this
											.setResult(RESULT_CANCELED);
									Util.showToast(ReplayActivity.this,
											"回复失败!");
									finish();
									overridePendingTransition(
											R.anim.slide_left_in,
											R.anim.slide_right_out);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								return;
							}
						}
					}
				}, Constant.DO_POST, "正在提交回复...");
			}
			break;
		default:
			break;
		}
	}
}
