package com.cofco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

import com.cofco.adapter.PageAdapter;
import com.cofco.builder.CreativeSquareBuilder;
import com.cofco.fragment.NewIdeaFragment;
import com.cofco.fragment.ProblemFragment;
import com.cofco.fragment.SolutionFragment;
import com.cofco.util.Constant;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;
/**
 * 弃用
 * 创意广场点击大图标进入的创意列表页面
 * @author sunbin
 *
 */
public class SquareListActivity extends BaseActivity{
	public static final String PAGE1_ID = "page1";
	public static final String PAGE2_ID = "page2";
	public static final String PAGE3_ID = "page3";
	private ArrayList<Fragment> fragments;
	private TabHost tabHost;  //TabHost
	private List<View> views;  //ViewPager内的View对象集合
	private FragmentManager manager;  //fragment管理器
	private ViewPager pager;  //ViewPager
	private PageAdapter pageAdapter;
	private ImageView mBackButton;
	private Fragment newFragment;//新创意页面
	private Fragment proFragment;//问题页面
	private Fragment solutionFragment;//解决方案页面
	private ArrayList<CreativeItem> creativeList ;
	private HashMap<String,ArrayList<CreativeItem>> mResultData = new HashMap<String, ArrayList<CreativeItem>>();
	private ArrayList<CreativeItem> problemList = new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> solutionList =new ArrayList<CreativeItem>();
	private ArrayList<CreativeItem> newIdeaList=new ArrayList<CreativeItem>(); ; 
	private HashMap mHashMap  = new HashMap<String, String>();
	
	private int type;
	
	@Override
	public void onCreate(Bundle paramBundle) {
		type = getIntent().getExtras().getInt("type");
		int tys = type;
		super.onCreate(paramBundle);
		
	}
	@Override
	protected void findViewById() {
		//初始化资源
		mBackButton = (ImageView) this.findViewById(R.id.back);
		pager = (ViewPager) findViewById(R.id.viewpager);
//		tabHost = (TabHost) findViewById(R.id.tab_host);
		//管理tabHost开始
		tabHost.setup();
		//传一个空的内容给TabHost，不能用上面两个fragment
		TabContentFactory factory = new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return new View(SquareListActivity.this);
			}
		};
		
		TabSpec tabSpec = tabHost.newTabSpec(PAGE1_ID);
		tabSpec.setIndicator(createTabView(R.drawable.tab_problem_selector));
        tabSpec.setContent(factory);
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec(PAGE2_ID);
        tabSpec.setIndicator(createTabView(R.drawable.tab_solution_selector));
        tabSpec.setContent(factory);
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec(PAGE3_ID);
        tabSpec.setIndicator(createTabView(R.drawable.tab_new_innovation_selector));
        tabSpec.setContent(factory);
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);
        //管理tabHost结束
        
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.square_idea_list);
	
	}

	@Override
	protected void processLogic() {
		//获取创意排行榜
		getCreativeList() ;
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
		}
	}
	public ArrayList<CreativeItem> getCreativeList() {
		String sorted_id = String.valueOf(type+1);
		mHashMap.put("sort_id", sorted_id);
		final RequestVo vo = new RequestVo();
		vo.mContext = SquareListActivity.this;
		vo.mRequestUrl =getString(R.string.creative_square);
		vo.mRequestDataMap = mHashMap;
		vo.mJsonBuilder = new CreativeSquareBuilder();
		getDataFromServer(vo, new DataCallback<String>()     {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				//在这里进行处理
				try {
					HashMap<String,String> checkResponse = vo.mJsonBuilder.checkResponse(paramObject);
					if(checkResponse.get("status").equals("200")){
						if(creativeList!=null){
							creativeList.clear();
						}
						
						mResultData = (HashMap<String, ArrayList<CreativeItem>>) vo.mJsonBuilder.parseJSON(paramObject);
						problemList = mResultData.get("problemList");
						solutionList = mResultData.get("solutionList");
						newIdeaList = mResultData.get("newIdeaList");
						manager = getSupportFragmentManager();
						views = new ArrayList<View>();
					    fragments = new ArrayList<Fragment>();
					    proFragment =ProblemFragment.newInstance(problemList);
					    solutionFragment = SolutionFragment.newInstance(solutionList);
					    newFragment =NewIdeaFragment.newInstance(newIdeaList);
					    fragments.add(proFragment);
					    fragments.add(solutionFragment);
					    fragments.add(newFragment);
				        //设置监听器和适配器
					    pageAdapter = new PageAdapter(getSupportFragmentManager(),fragments);
					    pager.setAdapter(pageAdapter);
					    pager.setOnPageChangeListener(new PageChangeListener());
					    tabHost.setOnTabChangedListener(new TabChangeListener());
					}
//					Toast.makeText(mContext, checkResponse.get("msg"), 1).show();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				 
				
			}
		}, Constant.DO_POST, "正在加载数据...");
		return creativeList;
	}
	/**
	 * 标签页点击切换监听器
	 * @author Administrator
	 *
	 */
	private class TabChangeListener implements OnTabChangeListener {
		@Override
		public void onTabChanged(String tabId) {
			if (PAGE1_ID.equals(tabId)) {
                pager.setCurrentItem(0);
            } else if (PAGE2_ID.equals(tabId)) {
                pager.setCurrentItem(1);
            }else if (PAGE3_ID.equals(tabId)) {
                pager.setCurrentItem(2);
               
            }
		}
	}
	
	/**
	 * ViewPager滑动切换监听器
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
			tabHost.setCurrentTab(position);
		}
	}
	
	/**
	 * 创建tab View
	 * @param string
	 * @return
	 */
	private View createTabView(int id) {
		View tabView = getLayoutInflater().inflate(R.layout.tab, null);
		ImageView button = (ImageView) tabView.findViewById(R.id.tab_item);
		button.setBackgroundResource(id);
		return tabView;
	}
	

}
