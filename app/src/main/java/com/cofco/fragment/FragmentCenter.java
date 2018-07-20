package com.cofco.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.CenterCreativeActivity;
import com.cofco.CenterFavoriteActivity;
import com.cofco.LoginActivity;
import com.cofco.MessageActivity;
import com.cofco.R;
import com.cofco.SettingActivity;
import com.cofco.ShareActivity;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.PostAsynTask;
import com.cofco.util.Util;
import com.cofco.vo.RequestVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 用户中心,我
 * 
 * @author sunbin
 * 
 */
public class FragmentCenter extends BaseFragment implements OnClickListener {
	private static final String TAG = "FragmentCenter";
	private RelativeLayout mLayoutUserProfile;// 用户资料
	private RelativeLayout mLayoutMyFavorite;// 我的收藏
	private RelativeLayout mLayoutMyMessage;// 我的消息
	private RelativeLayout mLayoutMyInnovation;// 我的创意
	private RelativeLayout mLayoutSetting;// 设置
	private RelativeLayout mLayoutExit;
	private ImageView mShareButton;
	private ImageView mUserThumb;// 用户头像
	private TextView mCurrentVersion;
	private TextView mUserName;// 用户名
	private TextView mUserPoint;// 用户积分
	private TextView mUserInnovationNumber;// 我的创意个数
	private TextView mMessageNum;// 消息数
	private ImageView mUpdateInfo;// 更新消息
	private FrameLayout mLayoutMessageNum;
	private SharedPreferences sp;
	// 用户头像修改相关变量
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	// private String ImageName;
	ProgressDialog progressDialog;
	private Bitmap photo;
	private String imagePeople_Url;
	private String uid;
	private HashMap mHashMap = new HashMap();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = myInflater.inflate(R.layout.frag_center, container, false);
		findViewById(view);
		return view;
	}

	@Override
	protected void findViewById(View view) {
		mLayoutUserProfile = (RelativeLayout) view
				.findViewById(R.id.layout_center_my_info);
		mLayoutMyFavorite = (RelativeLayout) view
				.findViewById(R.id.layout_center_favorite);
		mLayoutMyMessage = (RelativeLayout) view
				.findViewById(R.id.layout_center_message);
		mLayoutMyInnovation = (RelativeLayout) view
				.findViewById(R.id.layout_center_my_innovation);
		mLayoutSetting = (RelativeLayout) view
				.findViewById(R.id.layout_center_setting);
		mUserThumb = (ImageView) view.findViewById(R.id.center_user_image);
		mLayoutExit = (RelativeLayout) view
				.findViewById(R.id.layout_center_exit);
		mUserName = (TextView) view.findViewById(R.id.center_user_name);
		mUserPoint = (TextView) view.findViewById(R.id.center_user_point);
		mUserInnovationNumber = (TextView) view
				.findViewById(R.id.center_user_innovation_num);
		mCurrentVersion = (TextView) view
				.findViewById(R.id.center_current_version);
		mMessageNum = (TextView) view
				.findViewById(R.id.fragment_center_message_num);
		mUpdateInfo = (ImageView) view
				.findViewById(R.id.fragment_innovation_new);
		mLayoutMessageNum = (FrameLayout) view
				.findViewById(R.id.layout_fragment_center_message_num);
		mShareButton = (ImageView) view.findViewById(R.id.share_button);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		sp = getActivity()
				.getSharedPreferences("account", Context.MODE_PRIVATE);
		String username = sp.getString("username", "");
		String imageUrl = sp.getString("user_image", "");
		String creativeNum = sp.getString("creative_num", "");
		String creditNum = sp.getString("credit_num", "");
		uid = sp.getString("uid", uid);
		if (!imageUrl.equals("")) {
			String url = Constant.IMAGE_BASE_URL + imageUrl;
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.user_img).build();
			imageLoader.displayImage(url, mUserThumb, options);
		}
		String version = Util.getVersionNumber(getActivity());
		mCurrentVersion.setText("当前版本:" + version);
		mUserName.setText(username);
		mUserPoint.setText(creditNum + "分");
		mUserInnovationNumber.setText(creativeNum + "个");
		super.onActivityCreated(savedInstanceState);
		setListener();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		String imageUrl = sp.getString("user_image", "");
		if (!imageUrl.equals("")) {
			String url = Constant.IMAGE_BASE_URL + imageUrl;
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.user_img).build();
			imageLoader.displayImage(url, mUserThumb, options);
		}
		final boolean flag = sp.getBoolean("isNew", false);
		int num = sp.getInt("msgNum", 0);
		int newNum = sp.getInt("newNum", 0);
		if (flag) {
			if (newNum > 0) {
				mLayoutMessageNum.setVisibility(View.VISIBLE);
				mMessageNum.setText(String.valueOf(newNum));
			} else {
				mLayoutMessageNum.setVisibility(View.VISIBLE);
				mMessageNum.setText(String.valueOf(num));
			}
		} else {
			mLayoutMessageNum.setVisibility(View.INVISIBLE);
		}
		float versions = sp.getFloat("version", 0);
		if (versions > 0) {
			mUpdateInfo.setVisibility(View.VISIBLE);
		} else {
			mUpdateInfo.setVisibility(View.INVISIBLE);
		}
		super.onStart();
	}

	@Override
	protected void processLogic() {
	}

	@Override
	protected void setListener() {
		mLayoutUserProfile.setOnClickListener(this);
		mLayoutMyFavorite.setOnClickListener(this);
		mLayoutMyInnovation.setOnClickListener(this);
		mLayoutMyMessage.setOnClickListener(this);
		mLayoutSetting.setOnClickListener(this);
		mLayoutExit.setOnClickListener(this);
		mUserThumb.setOnClickListener(this);
		mShareButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_button:
			Intent intent = new Intent();
			intent.setClass(getActivity(), ShareActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_center_exit:
			SharedPreferences sp = getActivity().getSharedPreferences(
					"account", Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putBoolean("hasLogined", false);
			edit.apply();
			Intent exitIntent = new Intent();
			exitIntent.setClass(getActivity(), LoginActivity.class);
			startActivity(exitIntent);
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.layout_center_favorite:
			// 我的收藏
			Intent intentFavorite = new Intent();
			intentFavorite
					.setClass(getActivity(), CenterFavoriteActivity.class);
			startActivity(intentFavorite);
			getActivity().overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.layout_center_message:
			// 我的消息
			Intent intentMessage = new Intent();
			intentMessage.setClass(getActivity(), MessageActivity.class);
			startActivity(intentMessage);
			getActivity().overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.layout_center_my_info:
			// 个人信息
			break;
		case R.id.layout_center_my_innovation:
			// 我的创意
			Intent intentCreative = new Intent();
			intentCreative
					.setClass(getActivity(), CenterCreativeActivity.class);
			startActivity(intentCreative);
			getActivity().overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.layout_center_setting:
			Intent intentSetting = new Intent();
			intentSetting.setClass(getActivity(), SettingActivity.class);
			startActivity(intentSetting);
			getActivity().overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			// 设置
			break;
		// 用户修改头像
		case R.id.center_user_image:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("图片来源");
			builder.setNegativeButton("取消", null);
			builder.setItems(new String[] { "拍照", "相册" },
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case TAKE_PICTURE:
								// 拍照
								// ImageName = "/" + getStringToday() + ".jpg";

								// 调用系统的拍照功能
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								// .fromFile(new File(Environment
								// .getExternalStorageDirectory(),
								// ImageName)));
								startActivityForResult(intent, PHOTOHRAPH);
								break;
							case CHOOSE_PICTURE:
								// 调用系统相册
								Intent intentChoose = new Intent(
										Intent.ACTION_PICK, null);
								intentChoose
										.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												IMAGE_UNSPECIFIED);
								// 调用剪切功能
								startActivityForResult(intentChoose, PHOTOZOOM);
								break;
							}
						}
					});
			builder.create().show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			// File picture = new File(Environment.getExternalStorageDirectory()
			// + ImageName);
			// startPhotoZoom(Uri.fromFile(picture));

			Uri uri = null;

			if (data.getData() == null) {
				Bundle extras1 = data.getExtras();
				if (extras1 != null) {
					Bitmap photo = (Bitmap) extras1.getParcelable("data");
					ContentResolver cr = getActivity().getContentResolver();
					String path = MediaStore.Images.Media.insertImage(cr,
							photo, "myPhoto", "Photo");
					uri = Uri.parse(path);
				}
			} else {
				uri = data.getData();
			}

			startPhotoZoom(uri);
		}

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -
																		// 100)压缩文件
				InputStream isBm = new ByteArrayInputStream(
						stream.toByteArray());
				mUserThumb.setImageBitmap(photo);
				// 上传头像
				// uploadUserThumb();
				uploadUserImage();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 修改头像
	 */
	private void uploadUserImage() {
		String fileName = getStringToday() + ".png";
		File file = null;
		try {
			file = bitmapToFile(getActivity(), photo, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		mHashMap.put("uid", uid);
		RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = getString(R.string.modify_thumb);
		vo.mRequestDataMap = mHashMap;
		vo.mFile = file;
		getDataFromServer(vo, new DataCallback<String>() {
			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					if(paramObject==null){
						return;
					}
					JSONObject object = new JSONObject(paramObject);
					String status = object.getString("status");
					String msg = object.getString("msg");
					if (object.has("data")) {
						String data = object.getString("data");
						Editor edit = sp.edit();
						edit.putString("user_image", data);
						edit.commit();
						if (!data.equals("")) {
							String url = Constant.IMAGE_BASE_URL + data;
							DisplayImageOptions options = new DisplayImageOptions.Builder()
									.showImageOnFail(R.drawable.user_img)
									.build();
							imageLoader.displayImage(url, mUserThumb);
						}
					}
					Util.showToast(getActivity(), msg);
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		}, Constant.DO_POST_FILE, "正在提交...");
	}

	private File bitmapToFile(Context context, Bitmap bitmap, String filename)
			throws Exception {
		File f = new File(context.getCacheDir(), filename);
		f.createNewFile();
		// Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();

		// write the bytes in file
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(bitmapdata);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}
	private void showDialog(){
		AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("分享我的应用").setTitle("分享").create();
		 Window window = dialog.getWindow();
		  window.setGravity(Gravity.BOTTOM);
		  window.setWindowAnimations(R.style.mystyle); 
		  window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  dialog.show();  
		  
		}
	// 获取系统当前时间将照片命名为系统时间
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	// 开始裁剪图片
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}
	
	
}
