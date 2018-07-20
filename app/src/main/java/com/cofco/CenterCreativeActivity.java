package com.cofco;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.BaseActivity.DataCallback;
import com.cofco.adapter.CenterCollectAdapter;
import com.cofco.builder.CollectBuilder;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.view.HorizontalListView;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.R.color;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 我的创意
 * @author sunbin
 *
 */
public class CenterCreativeActivity extends BaseActivity{
	private ImageView mBackButton;
	private ListView mListView;
	private SharedPreferences sp;
	private String uid;
	private HashMap mHashMap = new HashMap<String,String>();
	private ArrayList<CreativeItem> creativeList;
	private CenterCollectAdapter collectAdapter;
	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mListView = (ListView) this.findViewById(R.id.list_my_innovation);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_my_creative);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
	    
	}
	
	@Override
	protected void processLogic() {
		getCollectList();
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
	private void getCollectList() {
		if(!NetUtil.hasNetwork(CenterCreativeActivity.this)){
			CommonUtil.showInfoTost(CenterCreativeActivity.this, getString(R.string.net_errors));
			return;
		}
		if(!uid.equals("")){
			mHashMap.put("uid", uid);
			final RequestVo vo = new RequestVo();
			vo.mContext = CenterCreativeActivity.this;
			vo.mRequestUrl= getString(R.string.my_creative);
			vo.mRequestDataMap = mHashMap;
			vo.mJsonBuilder = new CollectBuilder();
			getDataFromServer(vo, new DataCallback<String>() {
				@Override
				public void processData(String paramObject, boolean paramBoolean) {
					if(paramObject!=null){
						try {
							JSONObject object = new JSONObject(paramObject);
							String status = object.getString("status");
							if(status.equals("200")){
								if(object.getString("data")!=null&&!object.get("data").equals("")){
									creativeList = (ArrayList<CreativeItem>) vo.mJsonBuilder.parseJSON(paramObject);
								if(collectAdapter==null){
									collectAdapter = new CenterCollectAdapter(CenterCreativeActivity.this, creativeList,imageLoader);
									mListView.setAdapter(collectAdapter);
									mListView.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent, View view,
												int position, long id) {
											CreativeItem item = creativeList.get(position);
											Intent intent = new Intent();
											intent.setClass(CenterCreativeActivity.this, CreativeDetailActivity.class);
											intent.putExtra("creativeItem", item);
											startActivity(intent);
											overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
										}
									});
								}else{
									mListView.setAdapter(collectAdapter);
								}
								}
							}
								
						} catch (JSONException e) {
							e.printStackTrace();
							return;
						}
					}
				}
			}, Constant.DO_POST, "正在获取收藏列表");
		}else{
			Toast mToast =Toast.makeText(CenterCreativeActivity.this, "收藏列表获取失败", 0);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.setDuration(0);
			mToast.show();
		}
	
	}
	public void onThumbnailClick(View v) {
		Bitmap bitmap = v.getDrawingCache();
		if(bitmap==null){
			return;
		}
//		 全屏显示的方法
		 final Dialog dialog = new Dialog(this, android.R.style.Theme_NoTitleBar_Fullscreen);
		 ImageView imgView= new ImageView(this);
		 imgView.setImageBitmap(bitmap);
		 imgView.setBackgroundColor(color.background_dark);
		 dialog.setContentView(imgView);
		 dialog.show();
//		// 点击图片消失
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});     
	}
	
}
