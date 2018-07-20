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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 解决方案
 * @author bin
 *
 */
public class SolutionFragment extends BaseFragment {
	private static final String TAG = "SolutionFragment";
	private ListView mListView;
	private SquareAdapter squareAdapter;
	private int type;
	private ArrayList<CreativeItem> creativeList = new ArrayList<CreativeItem>();
	public static SolutionFragment newInstance(ArrayList<CreativeItem> creativeList) {
		SolutionFragment newFragment = new SolutionFragment();
		// 将MainActivity中传过来的int值进行接收，会在TestFragment类中的onCreate方法中进行一些初始化工作
		Bundle bundle = new Bundle();
			bundle.putSerializable("creativeList", creativeList);
		newFragment.setArguments(bundle);
		
		return newFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment2, container, false);
		findViewById(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setListener();
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
		mListView = (ListView) view.findViewById(R.id.list_fragment2);
	}
	@Override
	protected void processLogic() {
	}
	@Override
	protected void setListener() {
	}

}
