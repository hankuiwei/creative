package com.cofco.adapter;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Util;
import com.cofco.view.HorizontalListView;
import com.cofco.vo.CreativeItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CenterCreativeAdapter extends BaseAdapter{
private Context mContext;
private ArrayList<CreativeItem> creativeList;
private LayoutInflater mLayoutInflater;
private String[] imageList;
private ImageLoader imageLoader;
private String[] url = new String[4];
	public CenterCreativeAdapter(Context context, ArrayList<CreativeItem> creativeList,ImageLoader imageLoader) {
	this.mContext = context;
	this.creativeList = creativeList;
	this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	this.imageLoader = imageLoader;
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
		ViewHolder holder ;
		if(view==null){
		 view = mLayoutInflater.inflate(R.layout.center_collect_list_item, null);
		 holder = new ViewHolder();
		 holder.mCreativeTitle = (TextView) view.findViewById(R.id.center_creative_title);
		 holder.mCreativeThumb = (ImageView) view.findViewById(R.id.center_creative_image);
		 holder.mCreativeContent = (TextView) view.findViewById(R.id.center_collect_content);
		 holder.mCreativeTime = (TextView) view.findViewById(R.id.center_collect_date);
		 holder.mLayoutCreativeImages = (LinearLayout) view.findViewById(R.id.center_collect_image_list);
		 holder.mCreativeImage01 = (ImageView) view.findViewById(R.id.center_collect_item_image01);
		 holder.mCreativeImage01.setDrawingCacheEnabled(true);
		 holder.mCreativeImage02 = (ImageView) view.findViewById(R.id.center_collect_item_image04);
		 holder.mCreativeImage02.setDrawingCacheEnabled(true);
		 holder.mCreativeImage03 = (ImageView) view.findViewById(R.id.center_collect_item_image03);
		 holder.mCreativeImage03.setDrawingCacheEnabled(true);
		 holder.mCreativeImage04 = (ImageView) view.findViewById(R.id.center_collect_item_image04);
		 holder.mCreativeImage04.setDrawingCacheEnabled(true);
		 view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		CreativeItem item = creativeList.get(position);
		int id = item.getSort_id();
		if (id == 1) {
			//
			holder.mCreativeThumb.setImageResource(R.drawable.icon1);
		} else if (id == 2) {
			holder.mCreativeThumb.setImageResource(R.drawable.icon2);
		} else if (id == 3) {
			holder.mCreativeThumb.setImageResource(R.drawable.icon3);
		} else if (id == 4) {
			holder.mCreativeThumb.setImageResource(R.drawable.icon4);
		} else if (id == 5) {
			holder.mCreativeThumb.setImageResource(R.drawable.icon5);
		}
		holder.mCreativeTitle.setText(item.getCreative_name());
		holder.mCreativeContent.setText(item.getCreative_description());
		holder.mCreativeTime.setText(Util.getDate(item.getCreate_time()));
		String[] str = item.getPhoto();
		if(str.length>0){
			for(int i=0;i<str.length;i++){
				url[i] = Constant.IMAGE_BASE_URL+str[i];
			}
			holder.mLayoutCreativeImages.setVisibility(View.VISIBLE);
			if(str.length==1){
				imageLoader.displayImage(url[0], holder.mCreativeImage01);
			}else if(str.length==2){
				imageLoader.displayImage(url[0], holder.mCreativeImage01);
				imageLoader.displayImage(url[1], holder.mCreativeImage02);
			}else if(str.length==3){
				imageLoader.displayImage(url[0], holder.mCreativeImage01);
				imageLoader.displayImage(url[1], holder.mCreativeImage02);
				imageLoader.displayImage(url[2], holder.mCreativeImage03);
			}else if(str.length==4){
				imageLoader.displayImage(url[0], holder.mCreativeImage01);
				imageLoader.displayImage(url[1], holder.mCreativeImage02);
				imageLoader.displayImage(url[2], holder.mCreativeImage03);
				imageLoader.displayImage(url[3], holder.mCreativeImage04);
			}
		}
		
		return view;
	}
	static class ViewHolder{
		ImageView mCreativeThumb;
		TextView mCreativeTitle;
		TextView mCreativeContent;
		TextView mCreativeTime;
		LinearLayout mLayoutCreativeImages;
		ImageView mCreativeImage01;
		ImageView mCreativeImage02;
		ImageView mCreativeImage03;
		ImageView mCreativeImage04;
		
	}
}
