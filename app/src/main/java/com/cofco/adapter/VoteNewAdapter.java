package com.cofco.adapter;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.fragment.BaseFragment;
import com.cofco.util.Constant;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 投票列表
 * 
 * @author sunbin
 * 
 */
public class VoteNewAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CreativeItem> creativeList;
	private LayoutInflater mLayoutInflater;
	private Animation animation;
	private int selectIndex = -1;
	private onVoteClickListener voteClickListener;

	public interface onVoteClickListener {
		public void voteClick(CreativeItem item, int position);
	}

	public VoteNewAdapter(Context context,
			ArrayList<CreativeItem> creativeList, BaseFragment fragment) {
		this.mContext = context;
		this.creativeList = creativeList;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.voteClickListener = (onVoteClickListener) fragment;
	}

	public void setCreativeList(ArrayList<CreativeItem> creatives) {
		this.creativeList = creatives;
	}

	public void setVoteClickListener(onVoteClickListener voteClickListener) {
		this.voteClickListener = voteClickListener;
	}

	@Override
	public int getCount() {
		if (creativeList != null) {
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
	public View getView(final int position, View convertView, ViewGroup root) {
		View view = null;
		ViewHolder holder = null;
		final CreativeItem item = creativeList.get(position);
		if (Constant.PAGE_TAG.equals("newVote")) {
			if (view == null) {
				view = convertView;
				view = mLayoutInflater.inflate(R.layout.vote_new_list_item,
						null);
				holder = new ViewHolder();
				holder.mCreatveThumb = (ImageView) view
						.findViewById(R.id.square_vote_creative_image);
				holder.mCreativeTime = (TextView) view
						.findViewById(R.id.square_vote_creative_time);
				holder.mCreativeTitle = (TextView) view
						.findViewById(R.id.square_vote_creative_title);
				holder.mVoteImage = (ImageView) view
						.findViewById(R.id.square_vote_image);
				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
			}
			holder.mCreativeTime.setText(Util.getDate(item.getCreate_time()));
			holder.mCreativeTitle.setText(item.getCreative_name());
			if (selectIndex == position) {
				holder.mVoteImage.setImageResource(R.drawable.voted);
				Constant.isVoted = true;
			}
			int voteStatus = item.getStatus();
			switch (voteStatus) {
			case 0:
				holder.mVoteImage.setImageResource(R.drawable.vote);
				break;
			case 1:
				holder.mVoteImage.setImageResource(R.drawable.voted);
			default:
				break;
			}
			
		} else {
			if (view == null) {
				view = convertView;
				view = mLayoutInflater.inflate(R.layout.vote_history_list_item,
						null);
				holder = new ViewHolder();
				holder.mCreativeTime = (TextView) view
						.findViewById(R.id.square_vote_history_creative_time);
				holder.mCreativeTitle = (TextView) view
						.findViewById(R.id.square_vote_history_creative_title);
				holder.mVoteButton = (Button) view
						.findViewById(R.id.square_vote_history_image);
				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
			}
			// final CreativeItem item = creativeList.get(position);
			int voteNum = item.getVote_num();
			holder.mCreativeTime.setText(item.getCreate_time());
			holder.mCreativeTitle.setText(item.getCreative_name());
			holder.mVoteButton.setText(voteNum + "票");
		}
		if (holder.mVoteImage != null) {
			holder.mVoteImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					voteClickListener.voteClick(item, position);
				}
			});
		}
		return view;
	}

	public interface OnListItemClick {
		public void onClick(int position);
	}

	static class ViewHolder {
		ImageView mCreatveThumb;
		TextView mCreativeTitle;
		TextView mCreativeTime;
		ImageView mVoteImage;
		Button mVoteButton;
	}
	
	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

}
