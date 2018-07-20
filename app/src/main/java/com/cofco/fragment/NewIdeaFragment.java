package com.cofco.fragment;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.adapter.SquareAdapter;
import com.cofco.vo.CreativeItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 新创意页面
 * @author sunbin
 *
 */
public class NewIdeaFragment extends BaseFragment {
	
	private static final String TAG = "NewIdeaFragment";
	private ListView mListView;
	private SquareAdapter squareAdapter;
	private int type;
	private ArrayList<CreativeItem> creativeList = new ArrayList<CreativeItem>();
	public static NewIdeaFragment newInstance(ArrayList<CreativeItem> creativeList) {
		NewIdeaFragment newFragment = new NewIdeaFragment();
		// 将MainActivity中传过来的int值进行接收，会在TestFragment类中的onCreate方法中进行一些初始化工作
		Bundle bundle = new Bundle();
		bundle.putSerializable("creativeList", creativeList);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment3, container, false);
		findViewById(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		creativeList = (ArrayList<CreativeItem>) getArguments().getSerializable("creativeList");
		if(creativeList!=null){
			if(squareAdapter==null){
				squareAdapter = new SquareAdapter(getActivity(),creativeList);
				mListView.setAdapter(squareAdapter);
			}else{
				mListView.setAdapter(squareAdapter);
			}
		}
		
	}

	@Override
	protected void findViewById(View view) {
		mListView = (ListView) view.findViewById(R.id.list_fragment3);
	}

	@Override
	protected void processLogic() {
		creativeList = (ArrayList<CreativeItem>) getArguments().getSerializable("creativeList");
		if(squareAdapter==null){
			squareAdapter = new SquareAdapter(getActivity(),creativeList);
			mListView.setAdapter(squareAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getActivity(), "您点的是:"+position, 0).show();
				}
			});
		}else{
			mListView.notify();
		}
	}

	@Override
	protected void setListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
					//查看详创意详细页面
			}
		});
	}
}
