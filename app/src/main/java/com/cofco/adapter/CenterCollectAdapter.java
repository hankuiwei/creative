package com.cofco.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.Util;
import com.cofco.view.HorizontalListView;
import com.cofco.vo.CreativeItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class CenterCollectAdapter extends BaseAdapter{
private static final String TAG = "CenterCollectAdapter";
private Context mContext;
private ArrayList<CreativeItem> creativeList;
private LayoutInflater mLayoutInflater;
private String[] imageList;
private ImageLoader imageLoader;
private String[] url = new String[4];
	public CenterCollectAdapter(Context context, ArrayList<CreativeItem> creativeList,ImageLoader imageLoader) {
	this.mContext = context;
	this.creativeList = creativeList;
	this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	this.imageLoader = imageLoader;
	}

	public void setCreativeList(ArrayList<CreativeItem> creativeList) {
		this.creativeList = creativeList;
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
			holder.mCreativeThumb.setImageResource(R.drawable.product_creative_detail);
		} else if (id == 2) {
			holder.mCreativeThumb.setImageResource(R.drawable.tehnoloty_creative_detail);
		} else if (id == 3) {
			holder.mCreativeThumb.setImageResource(R.drawable.pack_creative_detail);
		} else if (id == 4) {
			holder.mCreativeThumb.setImageResource(R.drawable.market_creative_detail);
		} else if (id == 5) {
			holder.mCreativeThumb.setImageResource(R.drawable.other_creative_detail);
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
				holder.mCreativeImage01.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Toast.makeText(mContext, "I am Click!", 0).show();
					}
				});
				showImageView(url[0], holder.mCreativeImage01);
			}else if(str.length==2){
				showImageView(url[0], holder.mCreativeImage01);
				showImageView(url[1],  holder.mCreativeImage02);

			}else if(str.length==3){ 
				showImageView(url[0], holder.mCreativeImage01);
				showImageView(url[1], holder.mCreativeImage02);
				showImageView(url[2], holder.mCreativeImage03);
			}else if(str.length==4){
				showImageView(url[0], holder.mCreativeImage01);
				showImageView(url[1], holder.mCreativeImage02);
				showImageView(url[2], holder.mCreativeImage03);
				showImageView(url[3], holder.mCreativeImage04);
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
	private void showImageView(String url,final ImageView imageView){
		imageLoader.loadImage(url, new SimpleImageLoadingListener(){

			@Override
			public void onLoadingComplete(String imageUri, View view,
					final Bitmap loadedImage) {
				imageView.setImageBitmap(loadedImage);
				/*imageView.setScaleX(2.5f);
				imageView.setScaleY(2.5f);*/
				imageView.setVisibility(View.VISIBLE);
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						Toast.makeText(mContext, "clickme", 0).show();
						float density = Util.getScreenDensity(mContext);
						int bitmapWidth = loadedImage.getWidth();
						int bitmapHeight = loadedImage.getHeight();
						float scalex = density;
						float scaley = density;
						Matrix matrix = new Matrix();
						matrix.postScale(scalex, scaley);
						Bitmap resizeBitmap = Bitmap.createBitmap(loadedImage,0,0,bitmapWidth,bitmapHeight,matrix,true);
//						Drawable draw = imageView.getDrawable();
						onThumbnailClick(resizeBitmap);
					}
				});
				super.onLoadingComplete(imageUri, view, loadedImage);
			}
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				Logger.i(TAG, "图片下载失败!");
				super.onLoadingFailed(imageUri, view, failReason);
			}
		});
	}
	public void onThumbnailClick(Bitmap bitmap) {
		// Drawable drawable = v.getBackground();
		// 全屏显示的方法
		ImageView imageView = new ImageView(mContext);
//		imageView.setScaleType(ScaleType.CENTER);
		imageView.setImageBitmap(bitmap);
		final Dialog dialog = new Dialog(mContext,
				android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		dialog.setContentView(imageView);
		dialog.show();
		//
		// // 点击图片消失
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
