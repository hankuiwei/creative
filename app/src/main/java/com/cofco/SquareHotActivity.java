package com.cofco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cofco.adapter.PageAdapter;
import com.cofco.builder.CreativeSquareBuilder;
import com.cofco.fragment.NewIdeaFragment;
import com.cofco.fragment.ProblemFragment;
import com.cofco.fragment.SolutionFragment;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

/**
 * 热门创意 创意广场点击大图标进入的创意列表页面
 * 
 * @author sunbin
 * 
 */
public class SquareHotActivity extends BaseActivity {
	public static final String PAGE1_ID = "page1";
	public static final String PAGE2_ID = "page2";
	public static final String PAGE3_ID = "page3";
	private ArrayList<Fragment> fragments;
	private List<View> views; // ViewPager内的View对象集合
	private FragmentManager manager; // fragment管理器
	private ViewPager pager; // ViewPager
	private PageAdapter pageAdapter;
	private ImageView mBackButton;
	private Fragment newFragment;// 新创意页面
	private Fragment proFragment;// 问题页面
	private Fragment solutionFragment;// 解决方案页面
	private ArrayList<CreativeItem> creativeList;
	private HashMap<String, ArrayList<CreativeItem>> mResultData = new HashMap<String, ArrayList<CreativeItem>>();
	private ArrayList<CreativeItem> problemList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> solutionList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> newIdeaList = new ArrayList<CreativeItem>();;
	private HashMap mHashMap = new HashMap<String, String>();
	private String keyword;
	private int type;

	//
	private int placeOld = 1, placeNew = 1;
	private Animation animation;
	private int one = 0, two, three;
	private ImageView animationLineIv;
	private RelativeLayout questionRel, solveRel, newIdeaRel;
	private TextView newIdeaTv, solveTv, questionTv;
	private ImageView newIdeaIv, solveIv, questionIv;

	@Override
	public void onCreate(Bundle paramBundle) {
		type = getIntent().getExtras().getInt("type");
		int tys = type;
		super.onCreate(paramBundle);

	}

	@Override
	protected void findViewById() {
		// 初始化资源
		mBackButton = (ImageView) this.findViewById(R.id.back);
		pager = (ViewPager) findViewById(R.id.viewpager);

		// 3个点击按键
		questionRel = (RelativeLayout) findViewById(R.id.sort_item1);
		solveRel = (RelativeLayout) findViewById(R.id.sort_item2);
		newIdeaRel = (RelativeLayout) findViewById(R.id.sort_item3);
		// 动画
		animationLineIv = (ImageView) findViewById(R.id.animation_line);
		animationLineIv.setLayoutParams(new LinearLayout.LayoutParams(two, LayoutParams.WRAP_CONTENT));

		questionTv = (TextView) findViewById(R.id.tv_question);
		solveTv = (TextView) findViewById(R.id.tv_solve);
		newIdeaTv = (TextView) findViewById(R.id.tv_newidea);
		questionIv = (ImageView) findViewById(R.id.iv_question);
		solveIv = (ImageView) findViewById(R.id.iv_solve);
		newIdeaIv = (ImageView) findViewById(R.id.iv_newidea);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.square_idea_list);
		keyword = getIntent().getExtras().getString("keyword");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		two = dm.widthPixels / 3;
		three = two * 2;

	}

	@Override
	protected void processLogic() {
		// 获取创意排行榜

		getCreativeList(keyword);
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);

		questionRel.setOnClickListener(this);
		solveRel.setOnClickListener(this);
		newIdeaRel.setOnClickListener(this);

		pager.setOnPageChangeListener(new PageChangeListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;
		// 动画切换卡的监听
		case R.id.sort_item1:
			pager.setCurrentItem(0);
			break;
		case R.id.sort_item2:
			pager.setCurrentItem(1);
			break;
		case R.id.sort_item3:
			pager.setCurrentItem(2);
			break;
		}
	}

	public ArrayList<CreativeItem> getCreativeList(String keyword) {
		if (!NetUtil.hasNetwork(SquareHotActivity.this)) {
			CommonUtil.showInfoTost(SquareHotActivity.this, getString(R.string.net_errors));
			return null;
		}
		if (!keyword.equals("")) {
			mHashMap.put("keyword", keyword);
		} else {
			String sorted_id = String.valueOf(type + 1);
			mHashMap.put("sort_id", sorted_id);
		}

		final RequestVo vo = new RequestVo();
		vo.mContext = SquareHotActivity.this;
		vo.mRequestUrl = getString(R.string.creative_square);
		vo.mRequestDataMap = mHashMap;
		vo.mJsonBuilder = new CreativeSquareBuilder();
		getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				// 在这里进行处理
				try {
					HashMap<String, String> checkResponse = vo.mJsonBuilder.checkResponse(paramObject);
					if (checkResponse.get("status").equals("200")) {
						if (creativeList != null) {
							creativeList.clear();
						}

						mResultData = (HashMap<String, ArrayList<CreativeItem>>) vo.mJsonBuilder.parseJSON(paramObject);
						problemList = mResultData.get("problemList");
						solutionList = mResultData.get("solutionList");
						newIdeaList = mResultData.get("newIdeaList");
						manager = getSupportFragmentManager();
						views = new ArrayList<View>();
						fragments = new ArrayList<Fragment>();
						proFragment = ProblemFragment.newInstance(problemList);
						solutionFragment = SolutionFragment.newInstance(solutionList);
						newFragment = NewIdeaFragment.newInstance(newIdeaList);
						fragments.add(proFragment);
						fragments.add(solutionFragment);
						fragments.add(newFragment);
						// 设置监听器和适配器
						pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
						pager.setAdapter(pageAdapter);
						// tabHost.setOnTabChangedListener(new TabChangeListener());
					} else {
						Util.showToast(SquareHotActivity.this, "无相关创意");
						return;
					}
					// Toast.makeText(mContext, checkResponse.get("msg"),
					// 1).show();

				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}

			}
		}, Constant.DO_POST, "正在加载数据...");
		return creativeList;
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
		}

		@Override
		public void onPageSelected(int position) {
			clearselected();
			updateTopbarPosition(position + 1);
			startMoveAnimation(position + 1);
		}
	}

	/**
	 * 开始动画
	 * @author wuchx
	 * @date 2014-1-27 下午5:13:31
	 * @param i
	 */
	public void startMoveAnimation(int i) {
		placeOld = placeNew;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(150);
		animationLineIv.startAnimation(animation);
	}

	/**
	 * 更新动画位置坐标
	 * @author wuchx
	 * @date 2014-1-27 下午5:12:30
	 * @param i
	 */
	public void updateTopbarPosition(int i) {
		placeNew = i;

		switch (placeNew) {
		case 1:
			if (placeOld == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			} else if (placeOld == 3) {
				animation = new TranslateAnimation(three, one, 0, 0);
			} else if (placeOld == 1) {
				animation = new TranslateAnimation(one, one, 0, 0);
			}
			questionTv.setTextColor(Color.parseColor("#10bcec"));
			questionIv.setImageResource(R.drawable.icon_idea_square_question_selected);
			break;
		case 2:
			if (placeOld == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
			} else if (placeOld == 3) {
				animation = new TranslateAnimation(three, two, 0, 0);
			} else if (placeOld == 2) {
				animation = new TranslateAnimation(two, two, 0, 0);
			}
			solveTv.setTextColor(Color.parseColor("#10bcec"));
			solveIv.setImageResource(R.drawable.icon_idea_square_solve_selected);
			break;

		case 3:
			if (placeOld == 1) {
				animation = new TranslateAnimation(one, three, 0, 0);
			} else if (placeOld == 2) {
				animation = new TranslateAnimation(two, three, 0, 0);
			} else if (placeOld == 3) {
				animation = new TranslateAnimation(three, three, 0, 0);
			}
			newIdeaTv.setTextColor(Color.parseColor("#10bcec"));
			newIdeaIv.setImageResource(R.drawable.icon_idea_square_new_selected);
			break;
		default:
			break;
		}
	}

	/**
	 * 清除选中标志
	 * @author wuchx
	 * @date 2014-1-27 下午5:11:42
	 */
	public void clearselected() {
		switch (placeOld) {
		case 1:
			questionTv.setTextColor(Color.BLACK);
			questionIv.setImageResource(R.drawable.icon_idea_square_question_normal);
			break;
		case 2:
			solveTv.setTextColor(Color.BLACK);
			solveIv.setImageResource(R.drawable.icon_idea_square_solve_normal);
			break;
		case 3:
			newIdeaTv.setTextColor(Color.BLACK);
			newIdeaIv.setImageResource(R.drawable.icon_idea_square_new_normal);
			break;
		default:
			break;
		}
	}

}
