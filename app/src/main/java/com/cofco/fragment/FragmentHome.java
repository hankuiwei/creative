package com.cofco.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.CreativeDetailActivity;
import com.cofco.HomeActivity;
import com.cofco.InnovationDetailActivity;
import com.cofco.MessageActivity;
import com.cofco.R;
import com.cofco.adapter.CreativeAdapter;
import com.cofco.adapter.MessageAdapter;
import com.cofco.builder.CreativeListBuilder;
import com.cofco.db.DatabaseHelper;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.LoadImageAsynTask;
import com.cofco.util.NetUtil;
import com.cofco.util.PostAsynTask;
import com.cofco.util.UpdateManager;
import com.cofco.util.LoadImageAsynTask.DoGetExecute;
import com.cofco.util.PostAsynTask.DoPosteExecute;
import com.cofco.util.Logger;
import com.cofco.util.Util;
import com.cofco.view.BannerLayout;
import com.cofco.view.SlidButton;
import com.cofco.view.SlidButton.OnChangedListener;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.Message;
import com.cofco.vo.RequestVo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 需要使用不带参数的构造器，可以使用getActivity ()替换context参数 否则屏幕在旋转的时候会抛出异常： Caused by:
 * java.lang.InstantiationException: can't instantiate class
 * com.michael.fragment.FragmentExecute; no empty constructor
 * 
 * @see http
 *      ://stackoverflow.com/questions/7016632/unable-to-instantiate-fragment
 * */
public class FragmentHome extends BaseFragment {
	private static final String TAG = "FragmentHome";
	private DatabaseHelper databaseHelper = null;
	private int type;// 创意的类型
	private Context mContext;
	private ListView mListView;
	private BannerLayout mBannerLayout;
	private SlidButton mSlideButton;
	private ArrayList<CreativeItem> creativeList;
	private ArrayList<CreativeItem> productList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> technologyList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> marketingList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> packList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> otherList = new ArrayList<CreativeItem>();
	private CreativeAdapter adapter;
	private Dao<CreativeItem, Integer> creativeDao;
	private HashMap requestMap = new HashMap<String, String>();
	private HashMap mVersionMap = new HashMap<String, String>();
	private ArrayList<String> images = new ArrayList<String>();
	private ArrayList<Message> messages;
	private LoadImageAsynTask task;
	private ImageView mBannerImage;
	private SharedPreferences sp;
	private int messageNumber;

	public FragmentHome() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Logger.i(TAG, "onCreateView方法");
		if (container == null) {
			// Currently in a layout without a container, so no
			// reason to create our view.
			return null;
		}
		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = myInflater.inflate(R.layout.frag_home, container, false);
		findViewById(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CreativeItem item = creativeList.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), InnovationDetailActivity.class);
				intent.putExtra("creativeItem", item);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Logger.i(TAG, "onActivityCreated方法");
		super.onActivityCreated(savedInstanceState);
		setListener();// 设置监听
		if (!NetUtil.hasNetwork(getActivity())) {
			CommonUtil.showInfoTost(getActivity(), "请检查您的网络连接!");
			return;
		} else {
			getBanner();// 获取广告图
			getCreativeList();// 获取创意列表
		}
	}

	/**
	 * 获取广告图
	 */
	private void getBanner() {
		type = (Integer) getArguments().getInt("type");
		// mContext = getActivity().();
		RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = getString(R.string.get_banner);
		task = new LoadImageAsynTask(new DoGetExecute() {
			@Override
			public void doAfterPost(String result) {
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result);
						String status = object.getString("status");
						if (status.equals("200")) {
							if (object.has("data")) {
								JSONObject data = object.getJSONObject("data");
								JSONArray imageJAr = data.getJSONArray("imageBeans");
								for (int i = 0; i < imageJAr.length(); i++) {
									images.add(getString(R.string.app_host)
											+ imageJAr.getJSONObject(i).getString("imageUrl"));
								}
								Util.showImageView(getActivity(),
										mBannerLayout, imageLoader, images);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return;
					}
				} else {
					return;
				}
			}
		});
		task.execute(vo);
	}

	/**
	 * 获取创意列表
	 */
	private void getCreativeList() {
		if (!NetUtil.hasNetwork(getActivity())) {
			CommonUtil.showInfoTost(getActivity(), "请检查您的网络连接！");
			return;
		}
		final RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = getActivity().getString(R.string.creative_list);
		vo.mJsonBuilder = new CreativeListBuilder();
		super.getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				// 在这里进行处理
				try {
					HashMap<String, String> checkResponse = vo.mJsonBuilder
							.checkResponse(paramObject);
					if (checkResponse.get("status").equals("200")) {
						if (creativeList != null) {
							creativeList.clear();
						}
						creativeList = (ArrayList<CreativeItem>) vo.mJsonBuilder
								.parseJSON(paramObject);
						Constant.UPDATE_LIST = false;
						if (type == Constant.PRODUCT) {
							setProductList(creativeList);
							if (adapter == null) {
								adapter = new CreativeAdapter(productList,
										getActivity(), type);
								mListView.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
						} else if (type == Constant.TECHNOLOGY) {
							setTechnologyList(creativeList);
							adapter = new CreativeAdapter(technologyList,
									getActivity(), type);
							mListView.setAdapter(adapter);
						} else if (type == Constant.PACK) {
							setPackList(creativeList);
							adapter = new CreativeAdapter(packList,
									getActivity(), type);
							mListView.setAdapter(adapter);
						} else if (type == Constant.MARKTING) {
							setMarketingList(creativeList);
							adapter = new CreativeAdapter(marketingList,
									getActivity(), type);
							mListView.setAdapter(adapter);
						} else if (type == Constant.OTHER) {
							setOtherList(creativeList);
							adapter = new CreativeAdapter(otherList,
									getActivity(), type);
							mListView.setAdapter(adapter);
						} else if (type == Constant.ALL) {
							if (adapter == null) {
								if (getActivity() == null) {
									return;
								}
								adapter = new CreativeAdapter(creativeList,
										getActivity(), type);
								mListView.setAdapter(adapter);
							} else {
								adapter.setCreativeList(creativeList);
								adapter.notifyDataSetChanged();
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}

			}
		}, Constant.DO_GET, "正在获取创意列表");
	}

	@Override
	public void onResume() {
		if (Constant.UPDATE_LIST) {
			getCreativeList();
		}
		super.onResume();
		Logger.i(TAG, "onResume方法");
	}

	@Override
	public void onStart() {

		super.onStart();
	}

	@Override
	public void onStop() {
		Logger.i(TAG, "onStop()方法");
		super.onStop();
	}

	@Override
	public void onDetach() {
		Logger.i(TAG, "onDetach()方法");
		super.onDetach();
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void findViewById(View view) {
		mSlideButton = (SlidButton) view.findViewById(R.id.slide_button);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		FrameLayout.LayoutParams layoutParams = (LayoutParams) mSlideButton
				.getLayoutParams();
		int marginTop = (int) (((float) 107 / 800) * height);
		layoutParams.setMargins(0, marginTop, 0, 0);
		mSlideButton.setLayoutParams(layoutParams);

		mListView = (ListView) view.findViewById(R.id.list);
		FrameLayout.LayoutParams mlistViewlayoutParams = (LayoutParams) mListView
				.getLayoutParams();
		int mlistMarginTop = (int) (((float) 132 / 800) * height);
		mlistViewlayoutParams.setMargins(0, mlistMarginTop, 0, 0);
		mListView.setLayoutParams(mlistViewlayoutParams);
		mBannerLayout = (BannerLayout) view.findViewById(R.id.home_banner);
		mBannerImage = (ImageView) view.findViewById(R.id.banner_image);
	}

	@Override
	protected void processLogic() {

	}

	/**
	 * 设置监听
	 */
	@Override
	protected void setListener() {
		// 叶子的滑动监听
		mSlideButton.SetOnChangedListener(new OnChangedListener() {
			@Override
			public void OnChanged(boolean checkState) {
				if (checkState) {
					// Toast.makeText(getActivity(), "You are Draging",
					// 0).show();
					int index = mBannerLayout.getCurrentScreenIndex();
					int count = mBannerLayout.getChildCount();
					if (count > 1) {

					}
					if (index == mBannerLayout.getChildCount() - 1) {
						index = 0;
					} else {
						index = index + 1;
					}
					mBannerLayout.scrollToScreen(index);
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CreativeItem item = creativeList.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), CreativeDetailActivity.class);
				intent.putExtra("creativeItem", item);
				startActivity(intent);
			}

		});
	}

	/**
	 * 以下方法是对创意的总类,分别放到不同的arraylist里面
	 */
	public ArrayList<CreativeItem> getPackList() {
		return packList;
	}

	public void setPackList(ArrayList<CreativeItem> packList) {
		for (int i = 0; i < packList.size(); i++) {
			if (packList.get(i).getSort_id() == 3) {
				this.packList.add(packList.get(i));
			}
		}
	}

	public ArrayList<CreativeItem> getOtherList() {
		return otherList;
	}

	public void setOtherList(ArrayList<CreativeItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			if (otherList.get(i).getSort_id() == 5) {
				this.otherList.add(otherList.get(i));
			}
		}
	}

	public ArrayList<CreativeItem> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<CreativeItem> productList) {
		this.productList.clear();
		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).getSort_id() == 1) {
				this.productList.add(productList.get(i));
			}
		}
	}

	public ArrayList<CreativeItem> getTechnologyList() {
		return technologyList;
	}

	public void setTechnologyList(ArrayList<CreativeItem> technologyList) {
		this.technologyList.clear();
		for (int i = 0; i < technologyList.size(); i++) {
			if (technologyList.get(i).getSort_id() == 2) {
				this.technologyList.add(technologyList.get(i));
			}
		}
	}

	public ArrayList<CreativeItem> getMarketingList() {
		return marketingList;
	}

	public void setMarketingList(ArrayList<CreativeItem> marketingList) {
		this.marketingList.clear();
		for (int i = 0; i < marketingList.size(); i++) {
			if (marketingList.get(i).getSort_id() == 4) {
				this.marketingList.add(marketingList.get(i));
			}
		}
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(getActivity());
		}
		return databaseHelper;
	}

	@Override
	public void onDestroy() {
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
		super.onDestroy();
	}

}
