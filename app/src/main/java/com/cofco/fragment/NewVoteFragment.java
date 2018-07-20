package com.cofco.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.InnovationDetailActivity;
import com.cofco.R;
import com.cofco.VoteActivity;
import com.cofco.adapter.VoteNewAdapter;
import com.cofco.adapter.VoteNewAdapter.onVoteClickListener;
import com.cofco.builder.VoteListBuilder;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 新创意页面
 * 
 * @author sunbin
 * 
 */
public class NewVoteFragment extends BaseFragment implements onVoteClickListener{

	private static final String TAG = "NewVoteFragment";
	private ListView mListView;
	private static final String TAG_NEW = "ThisVote";
	private VoteNewAdapter squareAdapter;
	private SharedPreferences sp;
	private String uid;
	private boolean flag = false;
	private int type;
	private HashMap mHashMap = new HashMap();
	private int currentPage = 1;
	private LinearLayout mLoadingMore;
	private Thread mThread;
	private  int getVoteSize;
	private String order = "vote_num";
	private String result;
	private boolean loadable=true;
	private View mDividerLine;
	private ImageView mImageView;
	private HashMap<String, ArrayList<CreativeItem>> mVoteData = new HashMap<String, ArrayList<CreativeItem>>();
	private static ArrayList<CreativeItem> creativeList = new ArrayList<CreativeItem>();

	public static NewVoteFragment newInstance(
			ArrayList<CreativeItem> creativeList) {
		NewVoteFragment newFragment = new NewVoteFragment();
		// 将MainActivity中传过来的int值进行接收，会在TestFragment类中的onCreate方法中进行一些初始化工作

		Bundle bundle = new Bundle();
		bundle.putSerializable("creativeList", creativeList);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_vote_list, container, false);
		findViewById(view);
		sp = getActivity()
				.getSharedPreferences("account", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int size = creativeList.size();
		Logger.e("ListSize", String.valueOf(size));
//		if(mListView!=null){
//			mListView.addFooterView(mLoadingMore);
//		}
		if (creativeList.size() < 10) {
			loadable = false;
//			mLoadingMore.setVisibility(View.INVISIBLE);
		}
		if (squareAdapter == null) {
			squareAdapter = new VoteNewAdapter(getActivity(), creativeList,this);
			mListView.setAdapter(squareAdapter);
		} else {
			squareAdapter.setCreativeList(creativeList);
			mListView.setAdapter(squareAdapter);
			squareAdapter.notifyDataSetChanged();
		}

		setListener();

	}

	@Override
	public void onResume() {
		boolean flags = flag;
		if (flag) {
			// ((VoteActivity)getActivity()).getVoteList();
		}
		super.onResume();
	}

	@Override
	protected void findViewById(View view) {
		mListView = (ListView) view.findViewById(R.id.new_vote_list);
		mLoadingMore = (LinearLayout) getActivity().getLayoutInflater()
				.inflate(R.layout.loading_more, null);
		mLoadingMore.setId(1001);
	}

	@Override
	protected void processLogic() {
	}

	@Override
	protected void setListener() {
		this.mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				CreativeItem item = creativeList.get(position);
				Intent intent = new Intent();
				intent.putExtra("creativeItem", item);
				intent.setClass(getActivity(), InnovationDetailActivity.class);
				startActivity(intent);
			}
		});
		mListView.setOnScrollListener(mScrollListener);
	}

	public ArrayList<CreativeItem> getCreativeList() {
		return creativeList;
	}

	public void setCreativeList(ArrayList<CreativeItem> creativeList) {
		this.creativeList = creativeList;
	}

	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (mListView == null) {
				return;
			}
			if(totalItemCount==0||totalItemCount<10){
				return;
			}
			int lastVisiblePosition = mListView.getLastVisiblePosition();
			if ((lastVisiblePosition == totalItemCount-1)&&loadable) {
//				mLoadingMore.setVisibility(View.VISIBLE);
				Logger.i("lastVisiblePosition", String.valueOf(totalItemCount));
				Logger.i("totalItemCount", String.valueOf(totalItemCount));
				loadMoreData(currentPage + 1);
			}else{
				return;
			}
		}

	};
	
	private void loadMoreData(int page) {
		mHashMap.put("uid", uid);
		mHashMap.put("page", String.valueOf(2));
		mHashMap.put("orders", order);
		int footerNum= mListView.getFooterViewsCount();
//		if(footerNum<1){
//			mListView.addFooterView(mLoadingMore);
//		}
		final RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = TAG_NEW;
		vo.mRequestDataMap = mHashMap;
		if (squareAdapter == null) {
			return;
		}
		if (mThread == null || !mThread.isAlive()) {
			mThread = new Thread() {
				@Override
				public void run() {
					try {
						// 这里放你网络数据请求的方法，我在这里用线程休眠5秒方法来处理
						Thread.sleep(5000);
						result = NetUtil.doPost(vo);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
					Message message = new Message();
					message.what = 1;
					message.obj = result;
					handler.sendMessage(message);
				}
			};
			mThread.start();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				VoteListBuilder voteBuilder =	new VoteListBuilder();
				HashMap<String, String> checkResponse;
				try {
					checkResponse = voteBuilder
							.checkResponse(result);
				
				if (checkResponse.get("status").equals("200")) {
					mVoteData = (HashMap<String, ArrayList<CreativeItem>>) voteBuilder
							.parseJSON(result);
					ArrayList<CreativeItem> voteList = mVoteData.get("vote");
					int voteSize = voteList.size();
					if(voteSize<10){
						loadable = false;
					}
					
					addCreatives(voteList);
//					mListView.removeFooterView(mLoadingMore);
					if(squareAdapter!=null){
						int num = creativeList.size();
						Logger.e("creativeList的大小", String.valueOf(num));
						squareAdapter.setCreativeList(creativeList);
						Logger.e("squareAdapterSize", String.valueOf(squareAdapter.getCount()));
						squareAdapter.notifyDataSetChanged();
					}
				}
				// 重新刷新Listview的adapter里面数据
			
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				break;
			}
		}
	};
	private void addCreatives(ArrayList<CreativeItem> items){
		if(creativeList!=null){
			for(int i=0;i<items.size();i++){
				creativeList.add(items.get(i));
			}
		}
	}

	@Override
	public void voteClick(CreativeItem item,final int position) {
		int voteStatus = item.getStatus();
		if(voteStatus==1){
			return;
		}
		if(Constant.PAGE_TAG.equals("historyVote")){
			return;
		}
		// 进行投票
		int creative_id = item.getCreative_id();
		mHashMap.put("uid", uid);
		Logger.e("Vote进行投票的创意ID","creativeID"+creative_id);
		mHashMap.put("creative_id", String.valueOf(creative_id));
		RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = getString(R.string.do_vote);
		getDataFromServer(vo, new DataCallback<String>() {
			@Override
			public void processData(String paramObject,
					boolean paramBoolean) {
				if (paramObject == null) {
					Util.showToast(getActivity(), "投票失败!");
				} else {
					try {
						JSONObject object = new JSONObject(paramObject);
						String status = object.getString("status");
						if (status.equals("200")) {
							Util.showToast(getActivity(), "投票成功!");
							flag = true;
							 creativeList.get(position).setStatus(1);
						} else {
							Util.showToast(getActivity(), "投票失败!");
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return;
					}

				}
			}

		}, Constant.DO_POST, "正在提交数据...");
	}
}
