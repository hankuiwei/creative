package com.cofco;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;

import com.cofco.adapter.CreativeAdapter;
import com.cofco.builder.CreativeListBuilder;
import com.cofco.builder.CreativeRankingBuilder;
import com.cofco.builder.CreativeSquareBuilder;
import com.cofco.util.Constant;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 创意广场
 * 
 * @date 2013.11.24
 * @author bin
 * 
 */
public class SquareActivity extends BaseActivity {
	private static final String TAG = "SquareActivity";
	private int type;// 创意的类型
	private ImageView mBackButton;
	private EditText mSearchKeyWord;
	private ImageView mSearchButton;
	private ImageView mOtherButton;
	private ImageView mProductButton;
	private ImageView mTechnolotyButton;
	private ImageView mPackButton;
	private ImageView mMarketButton;
	private ImageView mVoteButton;
	private RelativeLayout mMoreButton;
	private ImageView mGoldenCheckButton;
	private ImageView mSilverCheckButton;
	private ImageView mBronzeCheckButton;
	private TextView mSquareGoldenTitle;
	private TextView mSquareGoldenTime;
	private TextView mSquareSilverTitle;
	private TextView mSquareSilverTime;
	private TextView mSquareBrozeTitle;
	private TextView mSquareBrozeTime;
	private TextView mSquareRankingTitle;
	private RelativeLayout mLayoutGoldenButton;
	private RelativeLayout mLayoutSilverButton;
	private RelativeLayout mLayoutBronzeButton;
	private ArrayList<CreativeItem> creativeList;
	private ArrayList<CreativeItem> productList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> technologyList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> marketingList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> packList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> otherList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> votedCreateives;
	private ArrayList<CreativeItem> otherCreatives;
	private HashMap<String, ArrayList<CreativeItem>> creativeHashMap;
	private HashMap<String, ArrayList<CreativeItem>> rankingHashMap;
	private HashMap mHashMap = new HashMap<String, String>();
	private boolean hasRankings = false;
	private String keyword = "";
	private int year;
	private int month;
	
	private SharedPreferences sp;

	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mSearchKeyWord = (EditText) this.findViewById(R.id.square_search_edit);
		mSearchButton = (ImageView) this
				.findViewById(R.id.square_search_button);
		mOtherButton = (ImageView) this.findViewById(R.id.square_other_button);
		mProductButton = (ImageView) this
				.findViewById(R.id.square_product_button);
		mTechnolotyButton = (ImageView) this
				.findViewById(R.id.square_technology_button);
		mPackButton = (ImageView) this.findViewById(R.id.square_pack_button);
		mMarketButton = (ImageView) this
				.findViewById(R.id.square_market_button);
		mVoteButton = (ImageView) this.findViewById(R.id.square_vote_button);
		mMoreButton = (RelativeLayout) this
				.findViewById(R.id.layout_ranking_title);
		mGoldenCheckButton = (ImageView) this
				.findViewById(R.id.square_golden_detail_check);
		mSilverCheckButton = (ImageView) this
				.findViewById(R.id.square_silver_detail_check);
		mBronzeCheckButton = (ImageView) this
				.findViewById(R.id.square_broze_detail_check);
		mSquareRankingTitle = (TextView) this
				.findViewById(R.id.square_ranking_title);
		mSquareGoldenTitle = (TextView) this
				.findViewById(R.id.square_golden_title);
		mSquareGoldenTime = (TextView) this
				.findViewById(R.id.square_golden_time);
		mSquareSilverTitle = (TextView) this
				.findViewById(R.id.square_silver_title);
		mSquareSilverTime = (TextView) this
				.findViewById(R.id.square_silver_time);
		mSquareBrozeTitle = (TextView) this
				.findViewById(R.id.square_bronze_title);
		mSquareBrozeTime = (TextView) this
				.findViewById(R.id.square_bronze_time);
		mLayoutGoldenButton = (RelativeLayout) this
				.findViewById(R.id.layout_golden_ranking01);
		mLayoutSilverButton = (RelativeLayout) this
				.findViewById(R.id.layout_silver_ranking01);
		mLayoutBronzeButton = (RelativeLayout) this
				.findViewById(R.id.layout_bronze_ranking01);
		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.layout_ranking);
		// layout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(getApplicationContext(), "sfds", 0).show();
		// }
		// });
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.frag_square);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		// month = calendar.get(Calendar.MONTH)+1;
		
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		
		getRandList();
	}

	@Override
	protected void processLogic() {
		mSquareRankingTitle.setText(year + "年创意排行榜");
	}

	/**
	 * 获取创意排行榜
	 */
	private void getRandList() {

		mHashMap.put("uid", sp.getString("uid", ""));
		final RequestVo vo = new RequestVo();
		vo.mContext = SquareActivity.this;
		vo.mRequestUrl = getString(R.string.ranking);
		vo.mRequestDataMap = mHashMap;
		vo.mJsonBuilder = new CreativeRankingBuilder();
		getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					HashMap<String, String> checkResponse = vo.mJsonBuilder.checkResponse(paramObject);
					if (checkResponse.get("status").equals("200")) {
						if (creativeList != null) {
							creativeList.clear();
						}
						hasRankings = true;
						if (rankingHashMap == null) {
							rankingHashMap = new HashMap<String, ArrayList<CreativeItem>>();
						}
						rankingHashMap = (HashMap<String, ArrayList<CreativeItem>>) vo.mJsonBuilder.parseJSON(paramObject);
						votedCreateives = (ArrayList<CreativeItem>) rankingHashMap.get("votehead");
						if (votedCreateives != null) {
							if (votedCreateives.size() == 1) {
								mSquareGoldenTitle.setText(votedCreateives.get(0).getCreative_name());
								mSquareGoldenTime.setText(Util.getDate(votedCreateives.get(0).getCreate_time()));
							} else if (votedCreateives.size() == 2) {
								mSquareGoldenTitle.setText(votedCreateives.get(0).getCreative_name());
								mSquareGoldenTime.setText(Util.getDate(votedCreateives.get(0).getCreate_time()));
								mSquareSilverTitle.setText(votedCreateives.get(1).getCreative_name());
								mSquareSilverTime.setText(Util.getDate(votedCreateives.get(1).getCreate_time()));
							} else if (votedCreateives.size() == 3) {
								mSquareGoldenTitle.setText(votedCreateives.get(0).getCreative_name());
								mSquareGoldenTime.setText(Util.getDate(votedCreateives.get(0).getCreate_time()));
								mSquareSilverTitle.setText(votedCreateives.get(1).getCreative_name());
								mSquareSilverTime.setText(Util.getDate(votedCreateives.get(1).getCreate_time()));
								mSquareBrozeTitle.setText(votedCreateives.get(2).getCreative_name());
								mSquareBrozeTime.setText(Util.getDate(votedCreateives.get(2).getCreate_time()));
							}
						}
						// Toast.makeText(mContext, checkResponse.get("msg"),
						// 1).show();
					} else if (checkResponse.get("status").equals("0")) {
						// 没有创意排行榜
						hasRankings = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, Constant.DO_POST, "正在加载");
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
		mSearchButton.setOnClickListener(this);
		mOtherButton.setOnClickListener(this);
		mPackButton.setOnClickListener(this);
		mProductButton.setOnClickListener(this);
		mTechnolotyButton.setOnClickListener(this);
		mMarketButton.setOnClickListener(this);
		mVoteButton.setOnClickListener(this);
		mMoreButton.setOnClickListener(this);
		mGoldenCheckButton.setOnClickListener(this);
		mSilverCheckButton.setOnClickListener(this);
		mBronzeCheckButton.setOnClickListener(this);
		mLayoutGoldenButton.setOnClickListener(this);
		mLayoutSilverButton.setOnClickListener(this);
		mLayoutBronzeButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		// Toast.makeText(getApplicationContext(), id+"ssdf", 0).show();
		switch (v.getId()) {

		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			break;
		case R.id.square_product_button:
			setIntent(Constant.PRODUCT);
			break;
		case R.id.square_technology_button:
			setIntent(Constant.TECHNOLOGY);
			break;
		case R.id.square_pack_button:
			setIntent(Constant.PACK);
			break;
		case R.id.square_market_button:
			setIntent(Constant.MARKTING);
			break;
		case R.id.square_other_button:
			setIntent(Constant.OTHER);
			break;
		case R.id.square_search_button:
			keyword = mSearchKeyWord.getText().toString().trim();
			if (keyword.equals("") || keyword == null) {
				Util.showToast(SquareActivity.this, getString(R.string.keyword));
				return;
			} else {
				setIntent(Constant.ALL);
			}
			break;
		case R.id.square_vote_button:
			Intent voteIntent = new Intent();
			voteIntent.setClass(SquareActivity.this, VoteActivity.class);
			startActivity(voteIntent);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.layout_ranking_title:
			// 查看···更多榜单
			Intent intent = new Intent();
			intent.setClass(SquareActivity.this, SquareRankingActivity.class);
			intent.putExtra("hasRankings", hasRankings);
			if (hasRankings) {
				intent.putExtra("rankHashMap", rankingHashMap);
			}
			startActivity(intent);
			hasRankings = false;
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.square_golden_detail_check:
			if (votedCreateives == null) {
				return;
			}
			if (votedCreateives.get(0) != null
					&& !votedCreateives.get(0).equals("")) {
				CreativeItem item = votedCreateives.get(0);
				Intent intentGolden = new Intent();
				intentGolden.setClass(SquareActivity.this,
						CreativeDetailActivity.class);
				intentGolden.putExtra("creativeItem", item);
				startActivity(intentGolden);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else {
				return;
			}

			break;
		case R.id.square_silver_detail_check:
			if (votedCreateives == null) {
				return;
			}else if(votedCreateives.size()<2){
				return;
			}
			if (votedCreateives.get(1) != null
					&& !votedCreateives.get(1).equals("")) {
				CreativeItem item = votedCreateives.get(0);
				Intent intentGolden = new Intent();
				intentGolden.setClass(SquareActivity.this,
						CreativeDetailActivity.class);
				intentGolden.putExtra("creativeItem", item);
				startActivity(intentGolden);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else {
				return;
			}
			break;
		case R.id.square_broze_detail_check:
			if (votedCreateives == null) {
				return;
			}else if(votedCreateives.size()<3){
				return;
			}
			if (votedCreateives.get(2) != null
					&& !votedCreateives.get(2).equals("")) {
				CreativeItem item = votedCreateives.get(2);
				Intent intentGolden = new Intent();
				intentGolden.setClass(SquareActivity.this,
						CreativeDetailActivity.class);
				intentGolden.putExtra("creativeItem", item);
				startActivity(intentGolden);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else {
				return;
			}
			break;
		case R.id.layout_golden_ranking01:
			// Toast.makeText(getApplicationContext(), "sfdsf", 0).show();
			if (votedCreateives == null) {
				return;
			}else if(votedCreateives.size()<1){
				return;
			}
			if (votedCreateives.get(0) != null
					&& !votedCreateives.get(0).equals("")) {
				CreativeItem item = votedCreateives.get(0);
				Intent intentGolden = new Intent();
				intentGolden.setClass(SquareActivity.this,
						CreativeDetailActivity.class);
				intentGolden.putExtra("creativeItem", item);
				startActivity(intentGolden);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else {
				return;
			}

			break;

		}

	}

	/*
	 * private void searchCreative(String keyword){ HashMap map = new HashMap();
	 * map.put("keyword", keyword); RequestVo vo = new RequestVo(); vo.mContext
	 * =this; vo.mRequestUrl = getString(R.string.app_host); vo.mRequestDataMap
	 * = map; getDataFromServer(vo, new DataCallback<String>() {
	 * 
	 * @Override public void processData(String paramObject, boolean
	 * paramBoolean) { if(paramObject!=null){ CreativeSquareBuilder builder =
	 * new CreativeSquareBuilder(); HashMap hashMap =
	 * builder.checkResponse(paramObject); String status = (String)
	 * hashMap.get("status"); if(status.equals("200")){ ArrayList<creativeList>
	 * builder.parseJSON(paramObject); } ArrayList<CreativeItem> creativeItems =
	 * } } }, Constant.DO_POST, "正在搜索..."); }
	 */
	private void setIntent(int type) {
		Intent intent = new Intent();
		intent.setClass(SquareActivity.this, SquareHotActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("keyword", keyword);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

}