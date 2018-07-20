package com.cofco.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.cofco.R;
import com.cofco.adapter.SquareAdapter;
import com.cofco.adapter.VoteHistoryAdapter;
import com.cofco.builder.VoteListBuilder;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 新创意页面
 * 
 * @author sunbin
 * 
 */
public class HistoryVoteFragment extends BaseFragment {
	private static final String TAG = "HistoryVoteFragment";
	private ListView mHistoryListView;
	private static final String TAG_HISTORY = "ThatVote";
	private VoteHistoryAdapter historyAdapter;
	private HashMap mHashMap = new HashMap();
	private int currentPage = 1;
	private LinearLayout mLoadingMore;
	private SharedPreferences sp;
	private static final String TAG_NEW = "ThatVote";
	private Thread mThread;
	private int getVoteSize;
	private String order = "vote_num";
	private String result;
	private boolean loadable = true;
	private int type;
	private String uid;
	private HashMap<String, ArrayList<CreativeItem>> mVoteData = new HashMap<String, ArrayList<CreativeItem>>();
	private ArrayList<CreativeItem> creativeList = new ArrayList<CreativeItem>();

	public static HistoryVoteFragment newInstance(
			ArrayList<CreativeItem> creativeList) {
		HistoryVoteFragment newFragment = new HistoryVoteFragment();
		// 将MainActivity中传过来的int值进行接收，会在TestFragment类中的onCreate方法中进行一些初始化工作
		Bundle bundle = new Bundle();
		bundle.putSerializable("creativeList", creativeList);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.history_vote_list, container,
				false);
		findViewById(view);
		sp = getActivity()
				.getSharedPreferences("account", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		 mLoadingMore = (LinearLayout) getActivity().findViewById(1001);
		 creativeList = (ArrayList<CreativeItem>) getArguments()
				.getSerializable("creativeList");
		if (creativeList != null) {
			if (creativeList.size() < 10) {
				loadable = false;
//				 mLoadingMore.setVisibility(View.INVISIBLE);
			}
			historyAdapter = new VoteHistoryAdapter(getActivity(), creativeList,
					type);
			mHistoryListView.setAdapter(historyAdapter);
		}
	}

	@Override
	protected void findViewById(View view) {
		mHistoryListView = (ListView) view.findViewById(R.id.history_vote_list);
	}

	@Override
	protected void processLogic() {
	}

	@Override
	protected void setListener() {
		mHistoryListView.setOnScrollListener(mScrollListener);
		mHistoryListView.setClickable(false);
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
			if (mHistoryListView == null) {
				return;
			}
			if (totalItemCount == 0) {
				return;
			}
			int lastVisiblePosition = mHistoryListView.getLastVisiblePosition();
			if ((lastVisiblePosition == totalItemCount - 1) && loadable) {
				// mListView.addFooterView(mLoadingMore);
				Logger.i("lastVisiblePosition", String.valueOf(totalItemCount));
				Logger.i("totalItemCount", String.valueOf(totalItemCount));
				loadMoreData(currentPage + 1);
			} else {
				return;
			}
		}
																																																																																																																																																																																		
	};
	private void loadMoreData(int page) {
		mHashMap.put("uid", uid);
		mHashMap.put("page", String.valueOf(2));
		mHashMap.put("orders", order);
//		int footerNum = mHistoryListView.getFooterViewsCount();
//		if (footerNum < 1) {
//			mHistoryListView.addFooterView(mLoadingMore);
//		}
		final RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = TAG_NEW;
		vo.mRequestDataMap = mHashMap;
		if (historyAdapter == null) {
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
				VoteListBuilder voteBuilder = new VoteListBuilder();
				HashMap<String, String> checkResponse;
				try {
					checkResponse = voteBuilder.checkResponse(result);

					if (checkResponse.get("status").equals("200")) {
						mVoteData = (HashMap<String, ArrayList<CreativeItem>>) voteBuilder
								.parseJSON(result);
						ArrayList<CreativeItem> voteList = mVoteData
								.get("vote");
						int voteSize = voteList.size();
						if (voteSize < 10) {
							loadable = false;
						}
						// if(voteList.size()>=1){
						// creativeList.addAll(voteList);
						// }
						addCreatives(voteList);
//						mHistoryListView.removeFooterView(mLoadingMore);
						if (historyAdapter != null) {
							int num = creativeList.size();
							Logger.e("creativeList的大小", String.valueOf(num));
							historyAdapter.setCreativeList(creativeList);
							Logger.e("squareAdapterSize",
									String.valueOf(historyAdapter.getCount()));
							historyAdapter.notifyDataSetChanged();
						}
					}
					// 重新刷新Listview的adapter里面数据
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void addCreatives(ArrayList<CreativeItem> items) {
		if (creativeList != null) {
			for (int i = 0; i < items.size(); i++) {
				creativeList.add(items.get(i));
			}
		}
	}
}
