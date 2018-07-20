package com.cofco;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.adapter.CommentAdapter;
import com.cofco.adapter.CreativeAdapter;
import com.cofco.builder.CreativeDetailBuilder;
import com.cofco.fragment.DeleteCommentFragment;
import com.cofco.fragment.ReplayCommentFragment;
import com.cofco.fragment.DeleteCommentFragment.onChangedListener;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.util.PostAsynTask;
import com.cofco.util.PostAsynTask.DoPosteExecute;
import com.cofco.util.Util;
import com.cofco.vo.Comment;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.RequestVo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 创意详情
 * 
 * @author sunbin
 * 
 */
public class InnovationDetailActivity extends BaseActivity {
	private static final String TAG = "CreativeDetailActivity";
	private ImageView mBackButton;// 返回按钮
	private ImageView mCreativeImage;// 创意类型
	private TextView mCreativeTitle;// 创意标题
	private TextView mCreativeContent;// 创意内容
	private TextView mCreativeTime;// 创建时间
	private ImageView mVoteImage;// 投票

	private TextView mVoteText;// 投票
	private ImageView mReplayImage;// 回复
	private TextView mReplayNum;// 回复数
	private ImageView mPraiseImage;// 攒
	private TextView mPraiseNum;// 攒的人数
	private ImageView mFavoriteImage;// 收藏
	private TextView mFavoriteNum;// 收藏的人数
	private RelativeLayout mLayoutVote;
	private RelativeLayout mLayoutReplay;
	private RelativeLayout mLayoutPraise;
	private RelativeLayout mLayoutFavorite;
	private LinearLayout mPictureLayout;
	private ImageView mPicture1;
	private ImageView mPicture2;
	private ImageView mPicture3;
	private ImageView mPicture4;
	private int creativeId;
	private ListView mListView;// 评论列表
	private CreativeItem creativeItem;
//	private ArrayList<Comment> comments;
	private CommentAdapter commentAdapter;
	private HashMap mHashMap = new HashMap<String, String>();
	private HashMap mResultMap;
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	private SharedPreferences sp;
	private String[] url = new String[4];
	private int creative_id;
	private static final int BACK = 0;
	private static final int VOTE = 1;
	private static final int REPLAY = 2;
	private static final int PRAISE = 3;
	private static final int FAVORITE = 4;
	private String uid;
	boolean collectStatus;
	int voteStatus = 0;
	boolean praiseStatus = false;
	boolean commentStatus = false;
	@Override
	protected void findViewById() {
		mBackButton = (ImageView) this.findViewById(R.id.back);
		mCreativeImage = (ImageView) this
				.findViewById(R.id.creative_detail_image);
		mCreativeTitle = (TextView) this
				.findViewById(R.id.creative_detail_title);
		mCreativeTime = (TextView) this.findViewById(R.id.creative_detail_date);
		mCreativeContent = (TextView) this
				.findViewById(R.id.creative_detail_content);
		mVoteImage = (ImageView) this
				.findViewById(R.id.creative_detail_vote_image);
		mVoteText = (TextView) this
				.findViewById(R.id.creative_detail_votable_text);
		mReplayImage = (ImageView) this
				.findViewById(R.id.creative_detail_replay_image);
		mPraiseImage = (ImageView) this
				.findViewById(R.id.creative_detail_praise_image);
		mPraiseNum = (TextView) this
				.findViewById(R.id.creative_detail_praise_text);
		mReplayNum = (TextView) this
				.findViewById(R.id.creative_detail_replay_text);
		mFavoriteImage = (ImageView) this
				.findViewById(R.id.creative_detail_favorite_image);
		mFavoriteNum = (TextView) this
				.findViewById(R.id.creative_detail_favorite_text);
		mPictureLayout = (LinearLayout) this
				.findViewById(R.id.layout_creative_detail_picture);
		mLayoutVote = (RelativeLayout) this.findViewById(R.id.layout_detail_vote);
		mLayoutReplay  = (RelativeLayout) this.findViewById(R.id.layout_detail_replay);
		mLayoutPraise = (RelativeLayout) this.findViewById(R.id.layout_detail_praise);
		mLayoutFavorite = (RelativeLayout) this.findViewById(R.id.layout_detail_favorite);
		mPicture1 = (ImageView) this.findViewById(R.id.creative_item_image01);
		mPicture2 = (ImageView) this.findViewById(R.id.creative_item_image02);
		mPicture3 = (ImageView) this.findViewById(R.id.creative_item_image03);
		mPicture4 = (ImageView) this.findViewById(R.id.creative_item_image04);
		mListView = (ListView) this.findViewById(R.id.creative_detail_list);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.creative_detial);
		mScreenHeight = Util.getScreenHeight(InnovationDetailActivity.this);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		Intent intent = getIntent();
		// 获取创意列表页面传过来的创意Id
		creativeItem = (CreativeItem) intent.getExtras().get("creativeItem");
		creative_id = creativeItem.getCreative_id();
		if (creative_id != 0) {
			mHashMap.put("creative_id", String.valueOf(creative_id));
		}
		getInnovationDetail();// 获取创意详情
	}

	@Override
	protected void processLogic() {
		int id = creativeItem.getSort_id();
		if (id == 1) {
			mCreativeImage
					.setImageResource(R.drawable.icon1);
		} else if (id == 2) {
			mCreativeImage
					.setImageResource(R.drawable.icon2);
		} else if (id == 3) {
			mCreativeImage
					.setImageResource(R.drawable.icon3);
		} else if (id == 4) {
			mCreativeImage
					.setImageResource(R.drawable.icon4);
		} else if (id == 5) {
			mCreativeImage
					.setImageResource(R.drawable.icon5);
		}
		String[] photos = creativeItem.getPhoto();
		if(photos!=null){
		if (photos.length > 0) {
			mPictureLayout.setVisibility(View.VISIBLE);
			for (int i = 0; i < photos.length; i++) {
				/*url[i] = Constant.IMAGE_BASE_URL
						+ photos[i];*/
				url[i] = getString(R.string.app_host)+ photos[i];
			}
			if (photos.length == 1) {
				showImageView(url[0], mPicture1);
			} else if (photos.length == 2) {
				showImageView(url[0], mPicture1);
				showImageView(url[1], mPicture2);
			} else if (photos.length == 3) {
				showImageView(url[0], mPicture1);
				showImageView(url[1], mPicture2);
				showImageView(url[2], mPicture3);

			} else if (photos.length == 4) {
				showImageView(url[1], mPicture2);
				showImageView(url[2], mPicture3);
				showImageView(url[3], mPicture4);
			}
		}}
		String creativeName = creativeItem.getCreative_name();
		mCreativeTitle.setText(creativeItem.getCreative_name());
		mCreativeContent.setText(creativeItem.getCreative_description());
		mCreativeTime.setText(creativeItem.getCreate_time());
		getInnovationDetail();
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
		mBackButton.setTag(BACK);
		mLayoutVote.setOnClickListener(this);
		mLayoutVote.setTag(VOTE);
		mLayoutReplay.setOnClickListener(this);
		mLayoutReplay.setTag(REPLAY);
		mLayoutPraise.setOnClickListener(this);
		mLayoutPraise.setTag(PRAISE);
		mLayoutFavorite.setOnClickListener(this);
		mLayoutFavorite.setTag(FAVORITE);
		mVoteImage.setOnClickListener(this);
		mVoteImage.setTag(VOTE);
		mReplayImage.setOnClickListener(this);
		mReplayImage.setTag(REPLAY);
		mFavoriteImage.setOnClickListener(this);
		mFavoriteImage.setTag(FAVORITE);
		mPraiseImage.setOnClickListener(this);
		mPraiseImage.setTag(PRAISE);
		mListView.setOnItemClickListener(commentListener);
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		switch (tag) {
		case BACK:
			// 返回
			finish();
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			break;
		case VOTE:
			// 进行投票
			if (voteStatus==1) {
				Util.showToast(InnovationDetailActivity.this,
						getString(R.string.voted_text),-(mScreenHeight/4));
				return;
			} else if(voteStatus==2){
				Util.showToast(InnovationDetailActivity.this, getString(R.string.unvotable), -(mScreenHeight/4));
			}else {
				Util.showToast(InnovationDetailActivity.this,
						getString(R.string.vote_text),-(mScreenHeight/4));
				// 进行投票
				RequestVo vo = new RequestVo();
				vo.mContext = InnovationDetailActivity.this;
				vo.mRequestUrl = getString(R.string.do_vote);
				vo.mRequestDataMap = mHashMap;
				voteStatus = 1;
				PostAsynTask task = new PostAsynTask(new DoPosteExecute() {

					@Override
					public void doAfterPost(String result) {
						if (result == null) {
							CommonUtil.showInfoDialog(
									InnovationDetailActivity.this, "");
						}
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getString("status");
							if (status.equals("200")) {
								Util.showToast(InnovationDetailActivity.this,
										"投票成功",-(mScreenHeight/4));
								mVoteText.setText("已投票");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				task.execute(vo);
				mVoteImage.setSelected(true);
			}
			break;
		case REPLAY:
			// 进行回复
			// 进行评论
			Intent intent = new Intent();
			intent.setClass(InnovationDetailActivity.this, CommentActivity.class);
			intent.putExtra("creative_item", creativeItem);
			startActivityForResult(intent, 0);
			break;
		case PRAISE:
			// 进行攒
			if(!NetUtil.hasNetwork(InnovationDetailActivity.this)){
				CommonUtil.showInfoTost(InnovationDetailActivity.this, getString(R.string.net_errors));
				return;
			}
			if (praiseStatus) {
				Util.showToast(InnovationDetailActivity.this,
						getString(R.string.praised_text),-(mScreenHeight/4));
				return;
			} else {
				Util.showToast(InnovationDetailActivity.this,
						getString(R.string.praise_text),-(mScreenHeight/4));
				String praiseNum = mPraiseNum.getText()
						.toString().trim();
				String num = String.valueOf(Integer
						.valueOf(praiseNum) + 1);
				mPraiseNum.setText(num);
				// 进行攒
				RequestVo vo = new RequestVo();
				vo.mContext = InnovationDetailActivity.this;
				vo.mRequestUrl = getString(R.string.do_praise);
				vo.mRequestDataMap = mHashMap;
				PostAsynTask task = new PostAsynTask(new DoPosteExecute() {

					@Override
					public void doAfterPost(String result) {
						if (result == null) {
							/*CommonUtil.showInfoDialog(
									InnovationDetailActivity.this, "");*/
							return;
						}
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getString("status");
							if (status.equals("200")) {
//								Util.showToast(mContext,
//										getString(R.string.praise_text));
								mPraiseImage.setSelected(true);
								praiseStatus=true;
								Constant.UPDATE_LIST = true;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							return;
						}
					}
				});
				task.execute(vo);
				mPraiseImage.setSelected(true);
			}
			break;
		case FAVORITE:
			// 进行收藏
			if(!NetUtil.hasNetwork(InnovationDetailActivity.this)){
				CommonUtil.showInfoTost(InnovationDetailActivity.this, getString(R.string.net_errors));
				return;
			}
			if (collectStatus) {
				Util.showToast(InnovationDetailActivity.this,
						getString(R.string.favorited_text),-(mScreenHeight/4));
				return;
			} else {
				Util.showToast(InnovationDetailActivity.this,
						getString(R.string.favorited),-(mScreenHeight/4));
				// 进行投票
				RequestVo vo = new RequestVo();
				vo.mContext = InnovationDetailActivity.this;
				vo.mRequestUrl = getString(R.string.do_collect);
				vo.mRequestDataMap = mHashMap;
				PostAsynTask task = new PostAsynTask(new DoPosteExecute() {

					@Override
					public void doAfterPost(String result) {
						if (result == null) {
//							CommonUtil.showInfoDialog(InnovationDetailActivity.this, "");
							return;
						}
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getString("status");
							if (status.equals("200")) {
								Constant.UPDATE_LIST = true;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							return;
						}
					}
				});
				task.execute(vo);
				collectStatus = true;
				mFavoriteImage.setSelected(true);
			}
			break;
		}
	}

	private void getInnovationDetail() {
		if(!NetUtil.hasNetwork(InnovationDetailActivity.this)){
			CommonUtil.showInfoTost(InnovationDetailActivity.this, getString(R.string.net_errors));
			return;
		}
		if (uid.equals("")) {
			return;
		}
		mHashMap.put("uid", String.valueOf(uid));
		final RequestVo vo = new RequestVo();
		vo.mContext = InnovationDetailActivity.this;
		vo.mRequestUrl = getString(R.string.collect_details);
		vo.mRequestDataMap = mHashMap;
		vo.mJsonBuilder = new CreativeDetailBuilder();
		getDataFromServer(vo, new DataCallback<String>() {
			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				if (paramObject != null && paramObject != null) {
					try {
						JSONObject object = new JSONObject(paramObject);
						String status = object.getString("status");
						String msg = object.getString("msg");
						if (status.equals("200")) {
							mResultMap = (HashMap) vo.mJsonBuilder
									.parseJSON(paramObject);
							if (mResultMap.containsKey("creativeItem")) {
								CreativeItem item = (CreativeItem) mResultMap
										.get("creativeItem");
								mReplayNum.setText(item.getComment_num());
								mPraiseNum.setText(item.getPraise_num());
								mFavoriteNum.setText(item.getCollect_num());
								collectStatus = item.isCollect_status();
								voteStatus = item.getVote_status();
							    praiseStatus = item.isPraise_status();
								 commentStatus = item.isComment_status();
								if (voteStatus==1) {
									mVoteImage.setSelected(true);
									mVoteText.setText("已投票");
								}else if(voteStatus==2){
									mVoteText.setText("不可投票");
								}else if(voteStatus==0){
									mVoteText.setText("可投票");
								}
								if (collectStatus) {
									mFavoriteImage.setSelected(true);
								}
								if (praiseStatus) {
									mPraiseImage.setSelected(true);
								}
								if (commentStatus) {
									mReplayImage.setSelected(true);
								}
							}
     							if (mResultMap.containsKey("comments")) {
     							 comments.clear();
								 comments = (ArrayList<Comment>) mResultMap
										.get("comments");
								if (commentAdapter == null) {
									commentAdapter = new CommentAdapter(
											InnovationDetailActivity.this,
											comments, imageLoader);
									mListView.setAdapter(commentAdapter);
								} else {
									commentAdapter.setComments(comments);
									commentAdapter.notifyDataSetInvalidated();
//								 	 commentAdapter.notifyDataSetChanged();
//									mListView.getAdapter().notify();
								}
							}
						} else if (status.equals("0")) {
							Util.showToast(InnovationDetailActivity.this, msg,-(mScreenHeight/4));
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}, Constant.DO_POST, "正在加载...");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			getInnovationDetail();
			break;
		case RESULT_CANCELED:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showImageView(String url, final ImageView imageView) {
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			Bitmap resizeBitmap = null;
			@Override
			public void onLoadingComplete(String imageUri, View view,
					final Bitmap loadedImage) {
				int bitmapWidth = loadedImage.getWidth();
				int bitmapHeight = loadedImage.getHeight();
				imageView.setVisibility(View.VISIBLE);
				if(bitmapHeight<bitmapWidth){
					Matrix matrix = new Matrix();
					matrix.reset();  
					matrix.setRotate(90); 
					resizeBitmap = Bitmap.createBitmap(loadedImage,0,0,bitmapWidth,bitmapHeight,matrix,true);
					imageView.setImageBitmap(resizeBitmap);
				}else{
					resizeBitmap = loadedImage;
				}
				imageView.setImageBitmap(resizeBitmap);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
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
		// 全屏显示的方法
		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(bitmap);
		final Dialog dialog = new Dialog(this,
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
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	private OnItemClickListener commentListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, final int position,
				long id) {
			Comment comment = comments.get(position);
//			Toast.makeText(CreativeDetailActivity.this, "点击我", 0).show();
			String uids = comment.getUserid();
			if(uid.equals(uids)){
				//如果是本人的评论,则弹出删除框
				DeleteCommentFragment dialog = DeleteCommentFragment.newInstance("评论", comment, 1, new onChangedListener() {
					@Override
					public void onDataChanged(boolean isDeleted) {
						getInnovationDetail();
					}
				});
				dialog.show(getSupportFragmentManager(), "comment");
			}else{
				//则弹出回复框
				ReplayCommentFragment replay = ReplayCommentFragment.newInstance("回复", comment, 1);
				replay.show(getSupportFragmentManager(), "replay");
			}
			
		}
	};

}
