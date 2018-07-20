package com.cofco.adapter;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.Util;
import com.cofco.vo.Comment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter{
	private static final String TAG = "CommentAdapter";
	private Context mContext;
	private ArrayList<Comment> comments;
	private LayoutInflater mLayoutInflater;
	private ImageLoader imageLoader;
	
	
	public CommentAdapter(Context context, ArrayList<Comment> comments,
			ImageLoader imageLoader) {
		super();
		this.mContext = context;
		this.comments = comments;
		this.imageLoader = imageLoader;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		View view = convertView;
		ViewHolder holder ;
		if(view==null){
			view = mLayoutInflater.inflate(R.layout.creative_detail_replay_item, null);
			holder = new ViewHolder();
			holder.mCommentUSerImage = (ImageView) view.findViewById(R.id.creative_detail__replay_user_image);
			holder.mCommentUserName = (TextView) view.findViewById(R.id.creative_detail__replay_user_name);
			holder.mCommentContent = (TextView) view.findViewById(R.id.creative_detail_replay_content);
			holder.mCommentTime  = (TextView) view.findViewById(R.id.creative_detail_replay_time);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		Comment comment = (Comment) getItem(position);
		String type = comment.getComment_type();
		if(type.equals("2")){
			holder.mCommentUserName.setText(comment.getUser_name() + "回复" +comment.getResive_name());
		}else{
			holder.mCommentUserName.setText(comment.getUser_name());
		}
		holder.mCommentTime.setText(Util.getDate(comment.getAdd_time()));
		holder.mCommentContent.setText(comment.getContent());
		
		String url = Constant.IMAGE_BASE_URL+Util.formatImgUrl(comment.getPhoto());
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.user_thumb).build();
		imageLoader.displayImage(url, holder.mCommentUSerImage, options);
		
		return view;
	}
	static class ViewHolder{
		TextView mCommentUserName;
		TextView mCommentContent;
		TextView mCommentTime;
		ImageView mCommentUSerImage;
	}
	private void showImageView(String url, final ImageView imageView) {
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
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
}
