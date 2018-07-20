package com.cofco.adapter;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 投票列表
 * @author sunbin
 *
 */
public class VoteHistoryAdapter extends BaseAdapter{
	private String tag = "VoteHistoryAdapter";
	
	private Context mContext ;
	private ArrayList<CreativeItem> creativeList;
	private LayoutInflater layoutInflater;
	private Animation animation;
	private int type ;
	public VoteHistoryAdapter(Context context,
			ArrayList<CreativeItem> creativeList,int type) {
		this.mContext = context;
		this.creativeList = creativeList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
	}
	@Override
	public int getCount() {
		if(creativeList!=null){
			return creativeList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return creativeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		View view = convertView;
		ViewHolder holder = null;

		if (view == null) {
				view = layoutInflater.inflate(R.layout.vote_history_list_item, null);
			holder = new ViewHolder();
			holder.mCreatveThumb = (ImageView) view
					.findViewById(R.id.square_vote_history_creative_image);
			holder.mCreativeTime = (TextView) view
					.findViewById(R.id.square_vote_history_creative_time);
			holder.mCreativeTitle = (TextView) view
					.findViewById(R.id.square_vote_history_creative_title);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CreativeItem item = creativeList.get(position);
		holder.mCreativeTime.setText(Util.getDate(item.getCreate_time()));
		holder.mCreativeTitle.setText(item.getCreative_name());
		int voteNumber = item.getVote_num();
		return view;
	}
	static class ViewHolder {
		ImageView mCreatveThumb;
		TextView mCreativeTitle;
		TextView mCreativeTime;
	}
	public ArrayList<CreativeItem> getCreativeList() {
		return creativeList;
	}
	public void setCreativeList(ArrayList<CreativeItem> creativeList) {
		this.creativeList = creativeList;
	}
	
}
