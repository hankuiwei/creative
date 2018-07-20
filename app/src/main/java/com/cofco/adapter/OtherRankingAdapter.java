package com.cofco.adapter;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.Util;
import com.cofco.vo.CreativeItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
/**
 * 其他创意排名适配器
 * @author sunbin
 *
 */
public class OtherRankingAdapter extends BaseAdapter{
	private static final String TAG = "OtherRankingAdapter";
	private Context mContext;
	private ArrayList<CreativeItem> otherCreativeList;
	private LayoutInflater mLayoutInflater;
	private ImageLoader mImageLoader;
	public OtherRankingAdapter(Context context,
			ArrayList<CreativeItem> otherCreativeList,ImageLoader imageLoader) {
		this.mContext = context;
		this.otherCreativeList = otherCreativeList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mImageLoader = imageLoader;
	}
	public void setOtherCreativeList(ArrayList<CreativeItem> otherCreativeList) {
		this.otherCreativeList = otherCreativeList;
	}
	
	@Override
	public int getCount() {
		if(otherCreativeList!=null){
			return otherCreativeList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return otherCreativeList.get(position);
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
			view = mLayoutInflater.inflate(R.layout.frag_square_ranking_item, null);
			holder = new ViewHolder();
			holder.mOtherImage = (ImageView) view.findViewById(R.id.square_other_image);
			holder.mOtherTitle = (TextView) view.findViewById(R.id.square_other_title);
			holder.mOtherTime = (TextView) view.findViewById(R.id.square_other_time);
			view.setTag(holder);
		}else{
			holder  = (ViewHolder) view.getTag();
		}
		CreativeItem item = otherCreativeList.get(position);
		/*if(!item.getPhoto().equals("")){
			String url = Constant.IMAGE_BASE_URL+item.getPhoto();
			showImageView(url, holder.mOtherImage);
		}*/
		holder.mOtherTitle.setText(item.getCreative_name());
		holder.mOtherTime.setText(Util.getDate(item.getCreate_time()));
		return view;
	}
	static class ViewHolder {
		ImageView mOtherImage;
		TextView mOtherTitle;
		TextView mOtherTime;
		TextView mOtherCheck;
	}
	private void showImageView(String url, final ImageView imageView) {
		mImageLoader.loadImage(url, new SimpleImageLoadingListener() {

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				imageView.setImageBitmap(loadedImage);
				imageView.setVisibility(View.VISIBLE);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						Toast.makeText(mContext, "clickme", 0).show();
						Drawable draw = imageView.getDrawable();
						onThumbnailClick(draw);
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

	public void onThumbnailClick(Drawable draw) {
		// Drawable drawable = v.getBackground();
		// 全屏显示的方法
		ImageView imageView = new ImageView(mContext);
		imageView.setScaleType(ScaleType.CENTER);
		imageView.setImageDrawable(draw);
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
