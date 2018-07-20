package com.cofco;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * @date 2313-12-3
 * @author bin
 *
 */
public class SettingActivity extends BaseActivity{
	private ImageView mBackButton;
	private  RelativeLayout mLayoutFeedBack;//用户反馈
	private RelativeLayout mLayoutAboutUs;//关于我们
	private RelativeLayout mLayoutAppUpdate;//软件升级
	private HashMap requestMap = new HashMap<String,String>();
	private HashMap mVersionMap = new HashMap<String,String>();
	private SharedPreferences sp;
	private ImageView mUpdateInfo;
	@Override
	protected void findViewById() {
		mBackButton =(ImageView) this.findViewById(R.id.back);
		mLayoutFeedBack= (RelativeLayout) this.findViewById(R.id.layout_center_feedback);
		mLayoutAboutUs = (RelativeLayout) this.findViewById(R.id.layout_center_about_us);
		mLayoutAppUpdate = (RelativeLayout) this.findViewById(R.id.layout_center_app_update);
		mUpdateInfo = (ImageView) this.findViewById(R.id.fragment_innovation_new);
	}

	@Override
	protected void loadViewLayout() {
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		setContentView(R.layout.center_setting);
		float versions = sp.getFloat("version", 0);
		if (versions > 0) {
			if(mUpdateInfo==null){
				mUpdateInfo = (ImageView) findViewById(R.id.fragment_innovation_new);
			}
			mUpdateInfo.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void processLogic() {
		
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
		mLayoutFeedBack.setOnClickListener(this);
		mLayoutAboutUs.setOnClickListener(this);
		mLayoutAppUpdate.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;
		case R.id.layout_center_about_us:
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, AboutActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		case R.id.layout_center_feedback:
			Intent intentFeedBack = new Intent();
			intentFeedBack.setClass(SettingActivity.this,CenterFeedBackActivity.class);
			startActivity(intentFeedBack);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		case R.id.layout_center_app_update:
			if(!NetUtil.hasNetwork(SettingActivity.this)){
				CommonUtil.showInfoTost(SettingActivity.this, getString(R.string.net_errors));
				return;
			}
			getVersionNumber();
			break;
		}
		
	}
	/**
	 * 获取版本信息，并更新
	 * @return
	 */
	public void getVersionNumber(){
		if(!NetUtil.hasNetwork(SettingActivity.this)){
			CommonUtil.showInfoTost(SettingActivity.this,getString(R.string.net_errors));
			return ;
		}
		String msg = "正在获取版本信息";
		String number = Util.getVersionNumber(getApplicationContext());
//		requestMap.put("andriod_version_number", number);
		RequestVo vo = new RequestVo();
		vo.mContext = getApplicationContext();
		vo.mRequestUrl =getString(R.string.version_num);
//		vo.mRequestDataMap = requestMap;
		getDataFromServer(vo, new DataCallback<String>() {
			
			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					JSONObject object = new JSONObject(paramObject);
					String status = object.getString("status");
					if(status.equals("2") || status.equals("200")){
						JSONObject data = object.getJSONObject("data");
						String versionNum = data.getString("andriod_number");
						mVersionMap.put("andriod_number", versionNum);
						String appUrl = data.getString("andriod_url");
						mVersionMap.put("appUrl", appUrl);
						UpdateManager manager = new UpdateManager(SettingActivity.this);
//						// 检查软件更新
						manager.checkUpdate(mVersionMap);
						
					}else if(status.equals("0")){
						Util.showToast(SettingActivity.this, "已是最新版本!");
					}else {
						return;
					}
				
					
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		}, Constant.DO_GET,msg);
		return ;
	}
	
}
