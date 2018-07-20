package com.cofco;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cofco.adapter.OtherRankingAdapter;
import com.cofco.builder.CreativeRankingBuilder;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.view.PopMenuText;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.QiInfoItem;
import com.cofco.vo.RequestVo;

/**
 * 更多创意排行榜
 * 
 * @date 2013-11-30
 * @author sunbin
 * 
 */
public class SquareRankingActivity extends BaseActivity {
	private static final String TAG = "SquareRankingActivity";
	private static final String DATE_PICKER_TAG = "date_picker_dialog_fragment";
	private ImageView mBackButton;
	private TextView mDatePicker;// 日期选择控件
	private TextView mGoldenTitle;
	private TextView mSilverTitle;
	private TextView mBronzeTitle;
	private TextView mGoldenTime;
	private TextView mSilverTime;
	private TextView mBronzeTime;
	private ImageView mGoldenCheckButton;
	private ImageView mSilverCheckButton;
	private ImageView mBronzeCheckButton;
	private TextView mNoOtherRankings;
	private LinearLayout mLayoutNoRankings;
	private LinearLayout mLayoutRankings;
	private ListView mListView;
	private ListView mendListView;
	private Dialog mdialog;
	private Calendar calendar = null;
	private OtherRankingAdapter otherAdapter;
	private OtherRankingAdapter endAdapter;
	private HashMap rankingHashMap;
	private ArrayList<CreativeItem> creativeList;
	private ArrayList<CreativeItem> otherCreatives;
	private ArrayList<CreativeItem> votedCreateives;
	private ArrayList<CreativeItem> endCreatives;
	private boolean hasRankings;
	private HashMap mHashMap = new HashMap<String, String>();
	private int year;
	private int month;

	private int qi = 5;
	private SharedPreferences sp;
	private ArrayList<QiInfoItem> qiInfoItems = new ArrayList<QiInfoItem>();
	private CheckBox moreRBtn;
	private LinearLayout otherll;
	private PopMenuText popMenu;
	private List qiNameItems = new ArrayList();
	private TextView tvTime;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.frag_square_other_ranking);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		popMenu = new PopMenuText(this, width);
	}
	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mDatePicker = (TextView) this.findViewById(R.id.square_date_text);
		mGoldenTitle = (TextView) this.findViewById(R.id.square_golden_title);
		mGoldenTime = (TextView) this.findViewById(R.id.square_golden_time);
		mSilverTitle = (TextView) this.findViewById(R.id.square_silver_title);
		mSilverTime = (TextView) this.findViewById(R.id.square_silver_time);
		mBronzeTitle = (TextView) this.findViewById(R.id.square_bronze_title);
		mBronzeTime = (TextView) this.findViewById(R.id.square_bronze_time);
		mGoldenCheckButton = (ImageView) this.findViewById(R.id.square_golden_detail_check);
		mSilverCheckButton = (ImageView) this.findViewById(R.id.square_silver_detail_check);
		mBronzeCheckButton = (ImageView) this.findViewById(R.id.square_broze_detail_check);
		mLayoutRankings = (LinearLayout) this.findViewById(R.id.layout_ranking);
		mListView = (ListView) this.findViewById(R.id.square_other_list);
		mendListView = (ListView) this.findViewById(R.id.square_end_list);
		mNoOtherRankings = (TextView) this.findViewById(R.id.square_no_other_text);
		mLayoutNoRankings = (LinearLayout) this.findViewById(R.id.layout_no_ranking);
		moreRBtn = (CheckBox) this.findViewById(R.id.rbtn_more);
		otherll = (LinearLayout) this.findViewById(R.id.layout_other);
		tvTime = (TextView)this.findViewById(R.id.tv_time);
	}
	@Override
	protected void processLogic() {
		getRankList();
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
		mDatePicker.setOnClickListener(this);
		mGoldenCheckButton.setOnClickListener(this);
		mSilverCheckButton.setOnClickListener(this);
		mBronzeCheckButton.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 进入详细页面
				CreativeItem item = otherCreatives.get(position);
				Intent intent = new Intent();
				intent.setClass(SquareRankingActivity.this, CreativeDetailActivity.class);
				intent.putExtra("creativeItem", item);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		});
		mendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 进入详细页面
				CreativeItem item = endCreatives.get(position);
				Intent intent = new Intent();
				intent.setClass(SquareRankingActivity.this, CreativeDetailActivity.class);
				intent.putExtra("creativeItem", item);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		});

		moreRBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mendListView.setVisibility(View.VISIBLE);
					otherll.setVisibility(View.GONE);
				} else {
					mendListView.setVisibility(View.GONE);
					otherll.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	// 左边下拉菜单的监听
	private void qiButtonListener(View v) {
		popMenu.clearItems();
		popMenu.addItems(qiNameItems);
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.showAsDropDown(v);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;
		case R.id.square_date_text:
			qiButtonListener((View) v.getParent());
			break;
		case R.id.square_golden_detail_check:
			if (votedCreateives == null) {
				return;
			} else if (votedCreateives.size() < 1) {
				return;
			} else {
				Intent intentGolden = new Intent();
				intentGolden.setClass(SquareRankingActivity.this, CreativeDetailActivity.class);
				intentGolden.putExtra("creativeItem", votedCreateives.get(0));
				startActivity(intentGolden);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
			break;
		case R.id.square_silver_detail_check:

			if (votedCreateives == null) {
				return;
			} else if (votedCreateives.size() < 2) {
				return;
			} else {
				Intent intentSilver = new Intent();
				intentSilver.setClass(SquareRankingActivity.this, CreativeDetailActivity.class);
				intentSilver.putExtra("creativeItem", votedCreateives.get(1));
				startActivity(intentSilver);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
			break;
		case R.id.square_broze_detail_check:
			if (votedCreateives == null) {
				return;
			} else if (votedCreateives.size() < 3) {
				return;
			} else {
				Intent intentSilver = new Intent();
				intentSilver.setClass(SquareRankingActivity.this, CreativeDetailActivity.class);
				intentSilver.putExtra("creativeItem", votedCreateives.get(2));
				startActivity(intentSilver);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
			break;
		}
	}

	// 弹出菜单监听器
	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			System.out.println("下拉菜单点击" + position);
			
			QiInfoItem qiInfoItem = qiInfoItems.get(position);
			
			clearSelected(parent);
			((RelativeLayout)view).getChildAt(0).setBackgroundResource(R.drawable.creative_ranking_qi_item_selected);
			qi = qiInfoItem.getQi();
			
			getRankList();
			popMenu.dismiss();
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String startTime=df.format(Long.valueOf(qiInfoItem.getStart_time())*1000);
			String endTime=df.format(Long.valueOf(qiInfoItem.getEnd_time())*1000);
			
			mDatePicker.setText(qiInfoItems.get(position).getDangqi_name());
			tvTime.setText(startTime + " - " + endTime);
			
		}
	};
	
	private void clearSelected(AdapterView<?> parent){
		for(int i = 0; i<parent.getCount(); i++){
			View view = parent.getChildAt(i);
			((RelativeLayout)view).getChildAt(0).setBackgroundResource(R.drawable.creative_ranking_qi_item_nomal);
		}
	}

	/**
	 * 修改datepicker
	 * 
	 * @param dp
	 */
	private void resetDatePicker(DatePicker dp) {
		try {
			for (int i = 0; i < 10; i++) {
				int f = i + 1;
			}
			Field f[] = dp.getClass().getDeclaredFields();
			for (Field field : f) {
				if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
					field.setAccessible(true);
					Object dayPicker = new Object();
					dayPicker = field.get(dp);
					((View) dayPicker).setVisibility(View.GONE);
				}
			}
		} catch (SecurityException e) {
			Logger.d("ERROR", e.getMessage());
			return;
		} catch (IllegalArgumentException e) {
			Logger.d("ERROR", e.getMessage());
			return;
		} catch (IllegalAccessException e) {
			Logger.d("ERROR", e.getMessage());
			return;
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * 获取排行榜
	 */
	private void getRankList() {
		if (!NetUtil.hasNetwork(SquareRankingActivity.this)) {
			CommonUtil.showInfoTost(SquareRankingActivity.this, getString(R.string.net_errors));
			return;
		}
		mHashMap.put("uid", sp.getString("uid", ""));
		mHashMap.put("qi", String.valueOf(qi));
		final RequestVo vo = new RequestVo();
		vo.mContext = SquareRankingActivity.this;
		vo.mRequestUrl = getString(R.string.ranking);
		vo.mRequestDataMap = mHashMap;
		vo.mJsonBuilder = new CreativeRankingBuilder();
		getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					HashMap<String, String> checkResponse = vo.mJsonBuilder.checkResponse(paramObject);
					if (checkResponse == null) {
						return;
					}
					if (checkResponse.get("status").equals("200")) {
						if (creativeList != null) {
							creativeList.clear();
						}
						// mLayoutRankings.setVisibility(View.VISIBLE);
						mLayoutNoRankings.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						mNoOtherRankings.setVisibility(View.GONE);
						hasRankings = true;
						rankingHashMap = (HashMap) vo.mJsonBuilder.parseJSON(paramObject);
						if (votedCreateives != null) {
							votedCreateives.clear();
						}
						if (otherCreatives != null) {
							otherCreatives.clear();
						}

						if (endCreatives != null) {
							endCreatives.clear();
						}
						
						if (qiNameItems != null) {
							qiNameItems.clear();
						}

						qiInfoItems = (ArrayList<QiInfoItem>) rankingHashMap.get("qiinfo");
						votedCreateives = (ArrayList<CreativeItem>) rankingHashMap.get("votehead");
						otherCreatives = (ArrayList<CreativeItem>) rankingHashMap.get("voteother");
						endCreatives = (ArrayList<CreativeItem>) rankingHashMap.get("voteend");
						QiInfoItem item = qiInfoItems.get(0);
						SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						String startTime=df.format(Long.valueOf(item.getStart_time())*1000);
						String endTime=df.format(Long.valueOf(item.getEnd_time())*1000);
						tvTime.setText(startTime+"-"+endTime);
						for (QiInfoItem qiInfoItem : qiInfoItems) {
							qiNameItems.add(qiInfoItem.getDangqi_name());
						}
						// Toast.makeText(mContext, checkResponse.get("msg"),
						// 1).show();
						if (rankingHashMap != null) {
							if (votedCreateives.size() == 1) {
								mGoldenTitle.setText(votedCreateives.get(0).getCreative_name());

								mGoldenTime.setText(Util.getDate(votedCreateives.get(0).getCreate_time()));
							} else if (votedCreateives.size() == 2) {
								mGoldenTitle.setText(votedCreateives.get(0).getCreative_name());
								mGoldenTime.setText(Util.getDate(votedCreateives.get(0).getCreate_time()));
								mSilverTitle.setText(votedCreateives.get(1).getCreative_name());
								mSilverTime.setText(Util.getDate(votedCreateives.get(1).getCreate_time()));
							} else if (votedCreateives.size() == 3) {
								mGoldenTitle.setText(votedCreateives.get(0).getCreative_name());
								mGoldenTime.setText(Util.getDate(votedCreateives.get(0).getCreate_time()));
								mSilverTitle.setText(votedCreateives.get(1).getCreative_name());
								mSilverTime.setText(Util.getDate(votedCreateives.get(1).getCreate_time()));
								mBronzeTitle.setText(votedCreateives.get(2).getCreative_name());
								mBronzeTime.setText(Util.getDate(votedCreateives.get(2).getCreate_time()));
							}
							if (endCreatives.size() > 0) {
								if (endAdapter == null) {
									endAdapter = new OtherRankingAdapter(SquareRankingActivity.this, endCreatives, imageLoader);
									mendListView.setAdapter(endAdapter);
								} else {
									endAdapter.setOtherCreativeList(endCreatives);
									endAdapter.notifyDataSetChanged();
								}
							}
							if (otherCreatives.size() > 0) {
								if (otherAdapter == null) {
									otherAdapter = new OtherRankingAdapter(SquareRankingActivity.this, otherCreatives, imageLoader);
									mListView.setAdapter(otherAdapter);
								} else {
									otherAdapter.setOtherCreativeList(otherCreatives);
									otherAdapter.notifyDataSetChanged();
								}
							}
						}
					} else if (checkResponse.get("status").equals("0")) {
						// 没有创意排行榜
						// mLayoutRankings.setVisibility(View.GONE);
						// mLayoutNoRankings.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
						mNoOtherRankings.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		}, Constant.DO_POST, "正在加载");
	}

	/**
	 * 日期选择对话框
	 * 
	 * @author sunbin
	 * 
	 */
	public class CustomDatePickDialog extends DatePickerDialog implements OnDateChangedListener {
		Calendar calendar = Calendar.getInstance();

		public CustomDatePickDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
			super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		}

		public CustomDatePickDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
		}

		@Override
		public void onDateChanged(DatePicker view, int year, int month, int day) {
		}

		@Override
		public void setTitle(CharSequence title) {
			super.setTitle("日期选择");
		}
	}

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group
	 * @return
	 */
	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * 获取系统SDK版本
	 * 
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}
}
