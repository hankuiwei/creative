package com.cofco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import com.cofco.adapter.PageAdapter;
import com.cofco.builder.VoteListBuilder;
import com.cofco.fragment.HistoryVoteFragment;
import com.cofco.fragment.NewVoteFragment;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

/**
 * 投票页面
 * 
 * @author sunbin
 * 
 */
public class VoteActivity extends BaseActivity {
	public static final String PAGE1_ID = "page1";
	public static final String PAGE2_ID = "page2";
	private static final String TAG_NEW = "ThisVote";
	private static final String TAG_HISTORY = "ThatVote";
	public String url = TAG_NEW;
	private ArrayList<Fragment> fragments;
	private TabHost tabHost; // TabHost
	private List<View> views; // ViewPager内的View对象集合
	private FragmentManager manager; // fragment管理器
	private SharedPreferences sp;
	private ViewPager pager; // ViewPager
	private PageAdapter pageAdapter;
	private ImageView mBackButton;
	private NewVoteFragment newVoteFragment;// 最新投票
	private HistoryVoteFragment historyVoteFragment;// 历史投票,已投票
	private ImageView mRightButton;
	private LinearLayout mLayoutFilter;
	private LinearLayout mLayoutMessageInfo;
	private TextView mMessagePrompt;
	// 以下是选择不同类型进行显示:按投票，按攒，按收藏,按回复数
	private ImageView mVoteButton;
	private ImageView mPraiseButton;
	private ImageView mFavoriteButton;
	private ImageView mReplayButton;
	private int pageNum = 1;
	private String tag = TAG_NEW;
	private ArrayList<CreativeItem> creativeList;
	private HashMap mVoteData = new HashMap();
	private ArrayList<CreativeItem> newVoteList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> historyList = new ArrayList<CreativeItem>();
	private HashMap mHashMap = new HashMap<String, String>();
	private String voteNum;
	private String creativeNum;
	private String uid;
	private View mDividerLine;
	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		pager = (ViewPager) findViewById(R.id.viewpager);
		tabHost = (TabHost) findViewById(R.id.tab_host);
		mRightButton = (ImageView) findViewById(R.id.right_button);
		mLayoutFilter = (LinearLayout) findViewById(R.id.select_title_bar);
		mVoteButton = (ImageView) findViewById(R.id.vote_voted_button);
		mPraiseButton = (ImageView) findViewById(R.id.vote_praise_button);
		mFavoriteButton = (ImageView) findViewById(R.id.vote_favorite_button);
		mReplayButton = (ImageView) findViewById(R.id.vote_replay_button);
		mLayoutMessageInfo = (LinearLayout) findViewById(R.id.vote_message_prompt);
		mMessagePrompt = (TextView) findViewById(R.id.message_prompt);
		mDividerLine = findViewById(R.id.divider);
		// 管理tabHost开始
//		tabHost.getTabWidget().setDividerDrawable(drawable);
		tabHost.setup();
		// 传一个空的内容给TabHost，不能用上面两个fragment
		TabContentFactory factory = new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return new View(VoteActivity.this);
			}
		};
		TabSpec tabSpec = tabHost.newTabSpec(PAGE1_ID);
		tabSpec.setIndicator(createTabView(R.drawable.vote_new_selector));
		tabSpec.setContent(factory);
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec(PAGE2_ID);
		tabSpec.setIndicator(createTabView(R.drawable.vote_history_selector));
		tabSpec.setContent(factory);
		tabHost.addTab(tabSpec);
		tabHost.setCurrentTab(0);
		pager.setOnPageChangeListener(new PageChangeListener());
		tabHost.setOnTabChangedListener(new TabChangeListener());
		// 管理tabHost结束
	}
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.square_vote_listss);
		sp = this.getSharedPreferences("account", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
	}

	@Override
	protected void processLogic() {
		getVoteList("", String.valueOf(pageNum));
	}

	@Override
	protected void setListener() {
		mRightButton.setOnClickListener(this);
		mVoteButton.setOnClickListener(this);
		mFavoriteButton.setOnClickListener(this);
		mReplayButton.setOnClickListener(this);
		mPraiseButton.setOnClickListener(this);
		mBackButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			break;
		case R.id.right_button:
			int visiable = mLayoutFilter.getVisibility();
			if (visiable == View.VISIBLE) {
				mLayoutFilter.setVisibility(View.GONE);
			} else {
				mLayoutFilter.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.vote_voted_button:
			getVoteList("vote_num", String.valueOf(pageNum));
			mLayoutFilter.setVisibility(View.GONE);
			break;
		case R.id.vote_favorite_button:
			getVoteList("collect_num", String.valueOf(pageNum));
			mLayoutFilter.setVisibility(View.GONE);
			break;
		case R.id.vote_praise_button:
			getVoteList("praise_num", String.valueOf(pageNum));
			mLayoutFilter.setVisibility(View.GONE);
			break;
		case R.id.vote_replay_button:
			getVoteList("comment_num", String.valueOf(pageNum));
			mLayoutFilter.setVisibility(View.GONE);
			break;
		}

	}

	/**
	 * 标签页点击切换监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class TabChangeListener implements OnTabChangeListener {
		@Override
		public void onTabChanged(String tabId) {
			if (PAGE1_ID.equals(tabId)) {
				url = TAG_NEW;
				getVoteList("", String.valueOf(pageNum));
				pager.setCurrentItem(0);
				Constant.PAGE_TAG = "newVote";
			} else if (PAGE2_ID.equals(tabId)) {
				url = TAG_HISTORY;
				
				if (historyList.size() < 1) {
					getVoteList("", String.valueOf(pageNum));
				}
				Constant.PAGE_TAG = "historyVote";
				pager.setCurrentItem(1);
			}
		}
	}

	/**
	 * ViewPager滑动切换监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int position) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			int a = arg0;
			int c = arg2;
		}

		@Override
		public void onPageSelected(int position) {
			int curre = position;
			tabHost.setCurrentTab(position);

		}
	}

	/**
	 * 创建tab View
	 * 
	 * @param string
	 * @return
	 */
	private View createTabView(int id) {
		View tabView = getLayoutInflater().inflate(R.layout.tab, null);
		ImageView button = (ImageView) tabView.findViewById(R.id.tab_item);
		button.setImageResource(id);
		return tabView;
	}

	public void getVoteList(String order, String page) {
		mHashMap.put("uid", uid);
		if (!order.equals("")) {
			mHashMap.put("orders", order);
		}
		if (!page.equals("")) {
			mHashMap.put("page", page);
		}
		final RequestVo vo = new RequestVo();
		vo.mContext = VoteActivity.this;
		vo.mRequestUrl = url;
		vo.mJsonBuilder = new VoteListBuilder();
		vo.mRequestDataMap = mHashMap;
		if (mHashMap.containsKey("orders")) {
			Logger.e("TAG", mHashMap.get("orders").toString());
		}
		getDataFromServer(vo, new DataCallback<String>() {
			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					HashMap<String, String> checkResponse = vo.mJsonBuilder
							.checkResponse(paramObject);
					if (checkResponse.get("status").equals("200")) {
						if (creativeList != null) {
							creativeList.clear();
						}
						mVoteData = (HashMap) vo.mJsonBuilder
								.parseJSON(paramObject);
						voteNum = (String) mVoteData.get("vote_num");
						creativeNum = (String) mVoteData.get("creative_num");
						if (creativeNum != null) {
							if (!creativeNum.equals("0")) {
								mMessagePrompt.setText("本期共有" + creativeNum
										+ "个创意参与投票," + "您拥有" + voteNum + "票");
								mLayoutMessageInfo.setVisibility(View.VISIBLE);
								mDividerLine.setVisibility(View.VISIBLE);
							}
						}
//						if (newVoteList.size() > 1) {
//							newVoteList.clear();
//						}
						if (tag.equals(TAG_NEW)) {
							newVoteList = (ArrayList<CreativeItem>) mVoteData
									.get("vote");
						} else if (tag.equals(TAG_HISTORY)) {
							historyList = (ArrayList<CreativeItem>) mVoteData
									.get("vote");
						}
						int len = newVoteList.size();
						// historyList = mVoteData.get("voteHistoryList");
						manager = getSupportFragmentManager();
						views = new ArrayList<View>();
						if (fragments == null) {
							fragments = new ArrayList<Fragment>();
							newVoteFragment = NewVoteFragment
									.newInstance(newVoteList);
							historyVoteFragment = HistoryVoteFragment
									.newInstance(historyList);
							newVoteFragment.setCreativeList(newVoteList);
							historyVoteFragment.setCreativeList(historyList);
							fragments.add(newVoteFragment);
							fragments.add(historyVoteFragment);
						} else if (fragments.size() > 1) {
//							fragments.clear();
							newVoteFragment.setCreativeList(newVoteList);
							historyVoteFragment.setCreativeList(historyList);
						}
						// 设置监听器和适配器
						//
						if (pageAdapter == null) {
							pageAdapter = new PageAdapter(manager, fragments);
							pager.setAdapter(pageAdapter);

						} else {
//							pageAdapter = new PageAdapter(manager, fragments);
							pager.setAdapter(pageAdapter);
						}
					} else if (checkResponse.get("status").equals("0")) {
						// 没有投票列表
						Toast mToast = Toast.makeText(VoteActivity.this,
								"暂无数据!", 0);
						mToast.setGravity(Gravity.CENTER, 0, -40);
						mToast.setDuration(0);
						mToast.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		}, Constant.DO_POST, "正在加载");
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	@Override
	protected void onStop() {
		Constant.PAGE_TAG = "newVote";
		super.onStop();
	}
	
}
