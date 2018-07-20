package com.cofco.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cofco.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
/**
 * 
 * @author sunbin
 *
 */
public class ShareAdapter extends BaseAdapter{
	private static final String LOG_TAG ="ShareAdapter";
	private ArrayList<String> shareItems = new ArrayList<String>();
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	public ShareAdapter(Context context) {
	   setShareItems();
	   this.mContext = context;
	   mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return shareItems.size();
	}
	@Override
	public Object getItem(int position) {
		return shareItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if(view==null){
			view = mLayoutInflater.inflate(R.layout.share_item, null);
			holder = new ViewHolder();
			holder.mShareImage = (ImageView) view.findViewById(R.id.share_image);
			holder.mShareTitle = (TextView) view.findViewById(R.id.share_title);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		String item = shareItems.get(position);
		switch (position) {
		case 0:
			holder.mShareImage.setImageResource(R.drawable.share_sinaweibo);
			holder.mShareTitle.setText(item);
			break;
		case 1:
			holder.mShareImage.setImageResource(R.drawable.share_tencentweibo);
			holder.mShareTitle.setText(item);
			break;
		case 2:
			holder.mShareImage.setImageResource(R.drawable.share_friend);
			holder.mShareTitle.setText(item);
			break;
		case 3:
			holder.mShareImage.setImageResource(R.drawable.share_email);
			holder.mShareTitle.setText(item);
			break;
		case 4:
			holder.mShareImage.setImageResource(R.drawable.share_sms);
			holder.mShareTitle.setText(item);
			break;
		default:
			break;
		}
		return view;
	}

	public void setShareItems() {
			shareItems.add("新浪微博");
			shareItems.add("腾讯微博");
			shareItems.add("朋友圈");
			shareItems.add("邮件");
			shareItems.add("短信");
	}
	static class ViewHolder{
		ImageView mShareImage;
		TextView mShareTitle;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
}
