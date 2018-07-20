package com.cofco.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cofco.BaseActivity;
import com.cofco.CreativeDetailActivity;
import com.cofco.InnovationDetailActivity;
import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.vo.CreativeItem;

/**
 * 创意列表adapter
 * 
 * @author sunbin
 * 
 */
public class CreativeAdapter extends BaseAdapter {
	private static final String TAG = "CreativeAdapter";
	private ArrayList<CreativeItem> creativeList;
	private int type;
	private Context mContext;
	private LayoutInflater layoutInflater;
	private Animation animation;
	
	public CreativeAdapter(ArrayList<CreativeItem> creativeList,
			Context context, int type) {
		this.creativeList = creativeList;
		this.mContext = context;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
	}

	public void setCreativeList(ArrayList<CreativeItem> creativeList) {
		this.creativeList = creativeList;
	}

	@Override
	public int getCount() {
		return creativeList.size();
	}

	@Override
	public Object getItem(int position) {
		Log.i(">>>", "position: " + position);
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
			view = layoutInflater.inflate(R.layout.list_item2, null);
			holder = new ViewHolder();
			holder.mCreatveThumb = (ImageView) view
					.findViewById(R.id.list_item_image);
			holder.mCreativeContent = (TextView) view
					.findViewById(R.id.list_item_content);
			holder.mCreativeTime = (TextView) view
					.findViewById(R.id.list_item_time);
			holder.mCreativeTitle = (TextView) view
					.findViewById(R.id.list_item_title);
			holder.mFavoriteNum = (TextView) view.findViewById(R.id.favorite_num);
			holder.mPraiseNum = (TextView) view.findViewById(R.id.praise_num);
			holder.mReplayNum = (TextView) view.findViewById(R.id.replay_num);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final CreativeItem item = (CreativeItem) getItem(position);
		ImageView limageView;
		ImageView rimageView;
		if (position % 2 == 0) {
			limageView = (ImageView) view.findViewById(R.id.left_corver);
			limageView.setVisibility(View.GONE);
			rimageView = (ImageView) view.findViewById(R.id.right_corver);
			rimageView.setVisibility(View.INVISIBLE);
		} else {
			limageView = (ImageView) view.findViewById(R.id.left_corver);
			limageView.setVisibility(View.INVISIBLE);
			rimageView = (ImageView) view.findViewById(R.id.right_corver);
			rimageView.setVisibility(View.GONE);
		}
		int id = item.getSort_id();// 创意类型
		Logger.i("创意类型:", "创意类型"+id);
		Logger.i("创意类型:", "创意类型内容"+item.getCreative_description());
		Logger.i("创意类型:", "创意类型标题"+item.getCreative_name());
		switch (type) {
		case Constant.ALL:
			if (id == 1) {
				//
				holder.mCreatveThumb.setImageResource(R.drawable.icon1);
			} else if (id == 2) {
				holder.mCreatveThumb.setImageResource(R.drawable.icon2);
			} else if (id == 3) {
				holder.mCreatveThumb.setImageResource(R.drawable.icon3);
			} else if (id == 4) {
				holder.mCreatveThumb.setImageResource(R.drawable.icon4);
			} else if (id == 5) {
				holder.mCreatveThumb.setImageResource(R.drawable.icon5);
			}
			holder.mCreativeContent.setText(item.getCreative_description());
			holder.mCreativeTime.setText(item.getCreate_time());
			holder.mCreativeTitle.setText(item.getCreative_name());
			holder.mReplayNum.setText(item.getComment_num());
			holder.mPraiseNum.setText(item.getPraise_num());
			holder.mFavoriteNum.setText(item.getCollect_num());
			break;
		case Constant.PRODUCT:
			if(id==1){
				holder.mCreatveThumb.setImageResource(R.drawable.icon1);
				holder.mCreativeContent.setText(item.getCreative_description());
				holder.mCreativeTime.setText(item.getCreate_time());
				holder.mCreativeTitle.setText(item.getCreative_name());
				holder.mReplayNum.setText(item.getComment_num());
				holder.mPraiseNum.setText(item.getPraise_num());
				holder.mFavoriteNum.setText(item.getCollect_num());
			}
			break;
		case Constant.TECHNOLOGY:
			if(id==2){
				Logger.i("创意类型位置:", "所在的位置"+position);
				Logger.i("创意类型1111:", "创意类型"+id);
				Logger.i("创意类型1111:", "创意类型内容"+item.getCreative_description());
				Logger.i("创意类型1111:", "创意类型标题"+item.getCreative_name());
				holder.mCreatveThumb.setImageResource(R.drawable.icon2);
				holder.mCreativeContent.setText(item.getCreative_description());
				holder.mCreativeTime.setText(item.getCreate_time());
				holder.mCreativeTitle.setText(item.getCreative_name());
				holder.mReplayNum.setText(item.getComment_num());
				holder.mPraiseNum.setText(item.getPraise_num());
				holder.mFavoriteNum.setText(item.getCollect_num());
			}
			break;
		case Constant.PACK:
			 if(id==3){
				 	Logger.i("创意类型1111:", "所在的位置"+position);
					holder.mCreatveThumb.setImageResource(R.drawable.icon3);
					holder.mCreativeContent.setText(item.getCreative_description());
					holder.mCreativeTime.setText(item.getCreate_time());
					holder.mCreativeTitle.setText(item.getCreative_name());
					holder.mReplayNum.setText(item.getComment_num());
					holder.mPraiseNum.setText(item.getPraise_num());
					holder.mFavoriteNum.setText(item.getCollect_num());
				}
			break;
		case Constant.MARKTING:
			if(type==Constant.MARKTING&&id==4){
				Logger.i("创意类型1111:", "所在的位置"+position);
				holder.mCreatveThumb.setImageResource(R.drawable.icon4);
				holder.mCreativeContent.setText(item.getCreative_description());
				holder.mCreativeTime.setText(item.getCreate_time());
				holder.mCreativeTitle.setText(item.getCreative_name());
				holder.mReplayNum.setText(item.getComment_num());
				holder.mPraiseNum.setText(item.getPraise_num());
				holder.mFavoriteNum.setText(item.getCollect_num());
			}
			break;
		case Constant.OTHER:
			Logger.i("创意类型1111:", "所在的位置"+position);
			if(id==5){
				holder.mCreatveThumb.setImageResource(R.drawable.icon5);
				holder.mCreativeContent.setText(item.getCreative_description());
				holder.mCreativeTime.setText(item.getCreate_time());
				holder.mCreativeTitle.setText(item.getCreative_name());
				holder.mReplayNum.setText(item.getComment_num());
				holder.mPraiseNum.setText(item.getPraise_num());
				holder.mFavoriteNum.setText(item.getCollect_num());
			}
			break;
			}
		if (position % 2 == 0) {
			animation = AnimationUtils.loadAnimation(mContext,
					R.anim.in_from_right);
		} else {
			animation = AnimationUtils.loadAnimation(mContext,
					R.anim.in_from_left);
		}
		;
		view.setAnimation(animation);
		view.startAnimation(animation);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, InnovationDetailActivity.class);
				intent.putExtra("creativeItem", item);
				mContext.startActivity(intent);
			}
		});
		return view;
	}

	static class ViewHolder {
		ImageView mCreatveThumb;
		TextView mCreativeTitle;
		TextView mCreativeContent;
		TextView mCreativeTime;
		TextView mPraiseNum;
		TextView mFavoriteNum;
		TextView mReplayNum;
	}

}
