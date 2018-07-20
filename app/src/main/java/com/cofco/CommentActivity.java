package com.cofco;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 评论页面
 * 
 * @author sunbin
 * 
 */
public class CommentActivity extends BaseActivity {
	private ImageView mBackButton;
	private ImageView mSubmitButton;
	private EditText mCommentContent;
	private SharedPreferences sp;
	private HashMap mHashMap = new HashMap<String, String>();
	private CreativeItem creativeItem;

	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mSubmitButton = (ImageView) this.findViewById(R.id.submit);
		mCommentContent = (EditText) this
				.findViewById(R.id.center_comment_content);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_comment);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		String user_code = sp.getString("user_code", "");
		creativeItem = (CreativeItem) getIntent().getExtras().get(
				"creative_item");
		int creative_id = creativeItem.getCreative_id();
		mHashMap.put("uid", uid);
		mHashMap.put("user_code", user_code);
		mHashMap.put("creative_id", String.valueOf(creative_id));
		
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
			if (!NetUtil.hasNetwork(CommentActivity.this)) {
				CommonUtil.showInfoTost(CommentActivity.this,
						getString(R.string.net_errors));
				return;
			}
			String content = mCommentContent.getText().toString().trim();
			if (!content.equals("")) {
				mHashMap.put("content", content);
				RequestVo vo = new RequestVo();
				vo.mContext = CommentActivity.this;
				vo.mRequestUrl = getString(R.string.add_comment);
				vo.mRequestDataMap = mHashMap;
				super.getDataFromServer(vo, new DataCallback<String>() {

					@Override
					public void processData(String paramObject,
							boolean paramBoolean) {
						if (paramObject == null) {
							Util.showToast(CommentActivity.this, "评论失败!");
						} else {
							try {
								JSONObject object = new JSONObject(paramObject);
								String status = object.getString("status");
								String msg = object.getString("msg");
								if (status.equals("200")) {
									Util.showToast(CommentActivity.this, msg);
									Intent intent = new Intent();
									CommentActivity.this.setResult(RESULT_OK);
									Constant.UPDATE_LIST = true;
									finish();
									overridePendingTransition(
											R.anim.slide_left_in,
											R.anim.slide_right_out);
								} else if (status.equals("0")) {
									Intent intent = new Intent();
									CommentActivity.this.setResult(RESULT_OK);
									Util.showToast(CommentActivity.this, msg);
									finish();
									overridePendingTransition(
											R.anim.slide_left_in,
											R.anim.slide_right_out);
								} else {
									Intent intent = new Intent();
									CommentActivity.this
											.setResult(RESULT_CANCELED);
									Util.showToast(CommentActivity.this,
											"评论失败!");
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
				}, Constant.DO_POST, "正在提交评论...");
			}
			break;
		default:
			break;
		}
	}

}
