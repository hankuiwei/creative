package com.cofco;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.adapter.CenterCollectAdapter;
import com.cofco.builder.CollectBuilder;
import com.cofco.fragment.DeleteDialogFragment;
import com.cofco.fragment.DeleteDialogFragment.onChangedListener;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 我的收藏页面
 * 
 * @author sunbin
 * 
 */
public class CenterFavoriteActivity extends BaseActivity {
	private ImageView mBackButton;
	private ListView mListView;
	private ImageView mSearchButton;
	private EditText mSearchText;
	private SharedPreferences sp;
	private String uid;
	private HashMap mHashMap = new HashMap<String, String>();
	private ArrayList<CreativeItem> creativeList;
	private CenterCollectAdapter collectAdapter;
	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mListView = (ListView) this.findViewById(R.id.list_my_favorite);
		mSearchText = (EditText) this.findViewById(R.id.center_search_edit);
		mSearchButton = (ImageView) this.findViewById(R.id.center_search_button);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_my_favorite);
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
		mSearchButton.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CreativeItem item = creativeList.get(position);
				Intent intent = new Intent();
				intent.setClass(CenterFavoriteActivity.this,
						CreativeDetailActivity.class);
				intent.putExtra("creativeItem", item);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			}

		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					final int position, long id) {

				CreativeItem item = null;
				if (creativeList != null) {
					item = creativeList.get(position);
				}
				DeleteDialogFragment dialog = DeleteDialogFragment.newInstance(
						"我的收藏", item.getCollect_id(), Constant.FAVORITE,
						new onChangedListener() {

							@Override
							public void onDataChanged(boolean isDeleted) {
								if (isDeleted) {
									creativeList.remove(position);
									collectAdapter
											.setCreativeList(creativeList);
									mListView.setAdapter(collectAdapter);
								} else {
									Util.showToast(CenterFavoriteActivity.this,
											"删除失败!");
									return;
								}
							}
						});
				dialog.show(getSupportFragmentManager(), "favorite");
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			break;
		case R.id.center_search_button:
			String keyword = mSearchText.getText().toString().trim();
			if (keyword == null || keyword.equals("")) {
				Toast mToast = Toast.makeText(CenterFavoriteActivity.this,
						"请输入查询类别!", 0);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.setDuration(0);
				mToast.show();
			} else {
				mHashMap.clear();
				mHashMap.put("keyword", keyword);
				getCollectList();
			}
		default:
			break;
		}
	}

	private void getCollectList() {
		if (!uid.equals("")) {
			mHashMap.put("uid", uid);
			final RequestVo vo = new RequestVo();
			vo.mContext = CenterFavoriteActivity.this;
			vo.mRequestUrl = getString(R.string.my_collect);
			vo.mRequestDataMap = mHashMap;
			vo.mJsonBuilder = new CollectBuilder();
			getDataFromServer(vo, new DataCallback<String>() {
				@Override
				public void processData(String paramObject, boolean paramBoolean) {
					if (paramObject != null) {
						try {
							JSONObject object = new JSONObject(paramObject);
							String status = object.getString("status");
							String msg = object.getString("msg");
							if (status.equals("200")) {
								if (object.getString("data") != null
										&& !object.get("data").equals("")) {
									creativeList = (ArrayList<CreativeItem>) vo.mJsonBuilder
											.parseJSON(paramObject);
									if (collectAdapter == null) {
										collectAdapter = new CenterCollectAdapter(
												CenterFavoriteActivity.this,
												creativeList, imageLoader);
										mListView.setAdapter(collectAdapter);
									} else {
										mListView.setAdapter(collectAdapter);
									}
								}
							} else if (status.equals("0")) {
								if (creativeList != null) {
									creativeList.clear();
								}
								if (collectAdapter != null) {
									collectAdapter
											.setCreativeList(creativeList);
									mListView.setAdapter(collectAdapter);
								}
								Toast mToast = Toast.makeText(
										CenterFavoriteActivity.this, msg, 0);
								mToast.setGravity(Gravity.CENTER, 0, 0);
								mToast.setDuration(0);
								mToast.show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
							return;
						}
					}
				}
			}, Constant.DO_POST, "正在获取收藏列表");
		} else {
			Toast mToast = Toast.makeText(CenterFavoriteActivity.this,
					"您还没有收藏创意", 0);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.setDuration(0);
			mToast.show();
		}

	}

	/*
	 * public class DeleteDialogFragment extends DialogFragment {
	 * 
	 * }
	 */
}
