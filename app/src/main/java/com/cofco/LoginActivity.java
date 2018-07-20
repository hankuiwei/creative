package com.cofco;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cofco.builder.CreativeListBuilder;
import com.cofco.builder.UserInfoBuilder;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.UpdateManager;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;
import com.cofco.vo.UserInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @date 2013.11.18
 * @author bin
 * 
 */
public class LoginActivity extends BaseActivity {
	private EditText mUserNameEdit;
	private EditText mPasswordEdit;
	private Button mConfirmButton;
	private HashMap requestMap = new HashMap<String, String>();
	private HashMap mVersionMap = new HashMap<String, String>();
	private ArrayList<CreativeItem> creativeList;
	private boolean hasLogined = false;
	private UserInfo userInfo;// 用户信息
	private SharedPreferences sp;
	private Toast mToast;
	private String msg;

	@Override
	protected void findViewById() {
		mUserNameEdit = (EditText) this.findViewById(R.id.user_name);
		mPasswordEdit = (EditText) this.findViewById(R.id.user_password);
		mConfirmButton = (Button) this.findViewById(R.id.confirm_button);
//		String username = sp.getString("username", "");
//		String password = sp.getString("passwd", "");
//		mUserNameEdit.setText(username);
//		mPasswordEdit.setText(password);

	}

	@Override
	protected void loadViewLayout() {
//		getVersionNumber();
		setContentView(R.layout.login);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		
		hasLogined = sp.getBoolean("hasLogined", false);
		if (hasLogined) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}
	@Override
	protected void processLogic() {
		String userName = sp.getString("user", "");
		String password = sp.getString("passwd", "");
		if (!userName.equals("") && !password.equals("")) {
			mUserNameEdit.setText(userName);
			mPasswordEdit.setText(password);
		}

	}
	@Override
	protected void setListener() {
		mConfirmButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_button:
			msg = "正在登陆..";
			final String userName = mUserNameEdit.getText().toString().trim();
			final String password = mPasswordEdit.getText().toString().trim();
			Editor edit = sp.edit();
			edit.putString("user", userName);
			edit.putString("passwd", password);
			edit.commit();
			// getCreativeList();
			if (userName.equals("") || null == userName) {
				Toast.makeText(LoginActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (password.equals("") || null == password) {
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
						.show();
			}
			if (password.equals("") || password == null) {
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if(!NetUtil.hasNetwork(LoginActivity.this)){
				CommonUtil.showInfoTost(LoginActivity.this, getString(R.string.net_errors));
				return;
			}
			requestMap.clear();
			requestMap.put("username", userName);
			//requestMap.put("passwd", password);
			requestMap.put("usercode", password);
			RequestVo vo = new RequestVo();
			vo.mContext = mContext;
			vo.mRequestDataMap = requestMap;
			vo.mRequestUrl = getString(R.string.login);
			vo.mJsonBuilder = new UserInfoBuilder();
			getDataFromServer(vo, new DataCallback<String>() {

				@Override
				public void processData(String paramObject, boolean paramBoolean) {
					try {
						JSONObject object = new JSONObject(paramObject);
						String status = object.getString("status");
						String msg = object.getString("msg");
						String datas = null;
						JSONObject data = null;
						if (object.has("data")) {
							datas = object.getString("data");
							if (datas != null && !datas.equals("")) {
								data = object.getJSONObject("data");
							}
						}
						if (status.equals("200")) {

							Editor edit = sp.edit();
							if (data != null) {
								userInfo = new UserInfo();
								userInfo = JSON.parseObject(datas,
										UserInfo.class);
								String username = data.getString("username");
								String user_code = data.getString("user_code");
								String uid = data.getString("uid");
								edit.putString("username",
										userInfo.getUsername());
								edit.putString("user_code",
										userInfo.getUser_code());
								edit.putString("uid", userInfo.getUid());
								edit.putString("creative_num",
										userInfo.getCreative_num());
								edit.putString("credit_num",
										userInfo.getCredit_num());
								edit.putString("user_image",
										Util.formatImgUrl(userInfo.getPhoto()));
								edit.putBoolean("hasLogined", true);
								edit.commit();

								Intent intent = new Intent();
								// intent.putExtra("creativeList",
								// creativeList);
								intent.setClass(mContext, HomeActivity.class);
								startActivity(intent);
								overridePendingTransition(
										R.anim.slide_right_in,
										R.anim.slide_left_out);
								mToast = new Toast(mContext);
								mToast.setGravity(Gravity.CENTER, 0, 0);
								mToast.makeText(LoginActivity.this, msg, 0)
										.show();
								finish();
							}
						} else if (status.equals("0")) {
							Toast mToast = Toast.makeText(LoginActivity.this,
									msg, 0);
							mToast.setGravity(Gravity.CENTER, 0, 0);
							mToast.setDuration(0);
							mToast.show();
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return;
					}

				}
			}, Constant.DO_POST, msg);
			break;
		}
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
	}
}
