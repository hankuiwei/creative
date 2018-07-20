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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofco.R;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.Util;
import com.cofco.view.ActionItem;
import com.cofco.view.PopMenu;
import com.cofco.view.TitlePopup;
import com.cofco.view.ToastTool;
import com.cofco.vo.RequestVo;

/**
 * 发布创意页面
 * 
 * @author sunbin
 * 
 */
public class FragmentInnovation extends BaseFragment implements OnClickListener {
	private static final String TAG = "FragmentInnovation";
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private RelativeLayout mLayoutLeftMeun;
	private RelativeLayout mLayoutRightMenu;
	private LinearLayout mLayoutCreativeThumb;
	private EditText mCreativeTitleEdit;
	private EditText mCreativeContentEdit;
	private ImageView mCreativeThumb01;
	private ImageView mCreativeThumb02;
	private ImageView mCreativeThumb03;
	private ImageView mCreativeThumb04;
	private ImageView mSubmitButton;
	private ImageView mLeftButtonSelected;
	private TextView mLeftButtonSelectedText;
	private ImageView mRightButtonSelected;
	private TextView mRightButtonSelectedText;
	private TitlePopup titlePopup;
	private PopMenu popMenu;
	private int isClicked;
	// 底部选择相关变量
	private RadioGroup mPublicRadioGroup;// 公开,非公开
	private RadioGroup mOriginalRadioGroup;// 原创，非原创
	private RadioButton mPublicButton;// 公开
	private RadioButton mPrivateButton;// 非公开
	private RadioButton mOriginalButton;// 原创
	private RadioButton mUnOriginalButton;// 非原创
	private String msg;
	// 用户头像修改相关变量
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
//	private String ImageName;
	ProgressDialog progressDialog;
	private Bitmap photo;
	private String imagePeople_Url;
	private String uid;
	private String user_code;
	private String username;
	private SharedPreferences sp;
	private int num = 0;
	private File[] files = new File[4];
	private boolean isLeftItemClicked = false;
	private boolean isReftItemClicked = false;
	private HashMap<String, String> mHashMap = new HashMap<String, String>();
	private boolean[] isSelected = { false, false };
	private int[] menuItemsLeft = { R.drawable.bg_left_idea_item,
			R.drawable.bg_left_idea_item2, R.drawable.bg_left_idea_item3,
			R.drawable.bg_left_idea_item4, R.drawable.bg_left_idea_item5 };
	private int[] menuItemsRight = { R.drawable.bg_right_idea_item1,
			R.drawable.bg_right_idea_item2, R.drawable.bg_right_idea_item3 };
	private Boolean[] flag = new Boolean[] { false, false, false, false };
	private int publicId = 1;
	private int originalId = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}
		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = myInflater.inflate(R.layout.frag_innovation, container,
				false);
		findViewById(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		popMenu = new PopMenu(getActivity(),width);
		sp = getActivity()
				.getSharedPreferences("account", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		user_code = sp.getString("user_code", "");
		username = sp.getString("username", "");
		setListener();
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void findViewById(View view) {
		mLayoutLeftMeun = (RelativeLayout) view
				.findViewById(R.id.layout_innovation_second_left);
		mLayoutRightMenu = (RelativeLayout) view
				.findViewById(R.id.layout_innovation_second_right);
		mLayoutCreativeThumb = (LinearLayout) view
				.findViewById(R.id.layout_creative_thumb);
		mCreativeTitleEdit = (EditText) view.findViewById(R.id.creative_title);
		mCreativeContentEdit = (EditText) view
				.findViewById(R.id.creative_content);
		mCreativeThumb01 = (ImageView) view.findViewById(R.id.creative_thumb01);
		mCreativeThumb02 = (ImageView) view.findViewById(R.id.creative_thumb02);
		mCreativeThumb03 = (ImageView) view.findViewById(R.id.creative_thumb03);
		mCreativeThumb04 = (ImageView) view.findViewById(R.id.creative_thumb04);
		mLeftButtonSelected = (ImageView) view
				.findViewById(R.id.menu_item_left_selected);
		mRightButtonSelected = (ImageView) view
				.findViewById(R.id.menu_item_right_selected);
		mRightButtonSelectedText = (TextView) view
				.findViewById(R.id.menu_item_right_selected_text);
		mLeftButtonSelectedText = (TextView) view
				.findViewById(R.id.menu_item_left_selected_text);
		mSubmitButton = (ImageView) view.findViewById(R.id.submit);
		mPublicRadioGroup = (RadioGroup) view
				.findViewById(R.id.radio_group_public_button);
		mPublicButton = (RadioButton) view
				.findViewById(R.id.radio_public_button);
		mPublicButton.setChecked(true);
		mPrivateButton = (RadioButton) view
				.findViewById(R.id.radio_no_public_button);
		mOriginalRadioGroup = (RadioGroup) view
				.findViewById(R.id.radio_group_original_button);
		mOriginalButton = (RadioButton) view
				.findViewById(R.id.radio_original_button);
		mOriginalButton.setChecked(true);
		mUnOriginalButton = (RadioButton) view
				.findViewById(R.id.radio_no_original_button);
	}

	/**
	 * 提交创意
	 * 
	 * @param vo
	 *            封装的参数
	 * @param requestType
	 *            请求的类型
	 * @param msg
	 */
	private void postCreatives(final RequestVo vo, int requestType, String msg) {
		super.getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					if (paramObject == null) {
						return;
					}
					if (!paramObject.equals("")) {
						JSONObject response = new JSONObject(paramObject);
						String msg = response.getString("msg");
						if (response.get("status").equals("200")) {
							// vo.mJsonBuilder.parseJSON(paramObject);
							ToastTool.toast(getActivity(), msg, "s");
							mCreativeThumb01
									.setImageResource(R.drawable.bg_idea);
							mCreativeThumb02.setVisibility(View.INVISIBLE);
							mCreativeThumb03.setVisibility(View.INVISIBLE);
							mCreativeThumb04.setVisibility(View.INVISIBLE);
							mCreativeThumb02
									.setImageResource(R.drawable.bg_idea);
							mCreativeThumb03
									.setImageResource(R.drawable.bg_idea);
							mCreativeThumb04
									.setImageResource(R.drawable.bg_idea);

						} else if (response.get("status").equals("0")) {
							Util.showToast(getActivity(), "添加创意失败！");
						}
					} else {
						Util.showToast(getActivity(), "添加创意失败！");
					}
				} catch (JSONException e) {
					Logger.e(TAG, e.toString());
					e.printStackTrace();
					return;
				}
			}
		}, requestType, msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_innovation_second_left:
			// 左边菜单项点击监听器
			leftButtonListener(v);
			break;
		case R.id.layout_innovation_second_right:
			// 右边菜单项点击监听器
			rightButtonListener(v);
			break;
		case R.id.menu_item_left_selected:
			leftButtonListener(mLayoutLeftMeun);
			break;
		case R.id.menu_item_right_selected:
			rightButtonListener(mLayoutRightMenu);
			break;
		case R.id.submit:
			if (isSelected[0] == false) {
				Util.showToast(getActivity(), "请选择类别!");
			}
			if (isSelected[1] == false) {

				Toast mToast = Toast.makeText(getActivity(), "选择标签!", 0);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.setDuration(0);
				mToast.show();
				return;
			}
			String creativeTitle = mCreativeTitleEdit.getText().toString()
					.trim();
			String creativeContent = mCreativeContentEdit.getText().toString()
					.trim();
			if (creativeTitle == null || creativeTitle.equals("")) {
				Toast mToast = Toast.makeText(getActivity(), "请填写标题!", 0);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.setDuration(0);
				mToast.show();
				return;
			} else if (creativeContent == null || creativeContent.equals("")) {
				Toast mToast = Toast.makeText(getActivity(), "请填写内容!", 0);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.setDuration(0);
				mToast.show();
				return;
			}
			mHashMap.put("username", username);
			mHashMap.put("uid", uid);
			mHashMap.put("user_code", user_code);
			mHashMap.put("creative_name", creativeTitle);
			mHashMap.put("creative_description", creativeContent);
			mHashMap.put("disclosure", String.valueOf(publicId));
			mHashMap.put("fanart", String.valueOf(originalId));
			RequestVo vo = new RequestVo();
			vo.mContext = getActivity();
			vo.mRequestUrl = getString(R.string.addCreative);// +"&user_code=wangqixian&uid=2314&username=王启贤&creative_type=2&creative_description=是哥哥改善哥哥飞天&sort_id=1&creative_name=如果好时光沙发上公司法";
			vo.mRequestDataMap = mHashMap;
			if (files.length > 0) {
				vo.mFiles = files;
			}
			// 进行提交数据
			isSelected[0] = false;// 将左边菜单重置
			isSelected[1] = false;
			postCreatives(vo, Constant.Do_POST_FILES, msg);
			mLeftButtonSelected.setVisibility(View.GONE);
			mRightButtonSelected.setBackgroundDrawable(null);
			mLeftButtonSelectedText.setVisibility(View.VISIBLE);
			// mLeftButtonSelectedText.setText("选择类型");
			mRightButtonSelected.setVisibility(View.GONE);
			mRightButtonSelectedText.setVisibility(View.VISIBLE);
			mCreativeContentEdit.setText("");
			mCreativeTitleEdit.setText("");
			break;
		case R.id.creative_thumb01:
			num = 1;
			showPictureDialog();
			break;
		case R.id.creative_thumb02:
			num = 2;
			showPictureDialog();
			break;
		case R.id.creative_thumb03:
			num = 3;
			showPictureDialog();

			break;
		case R.id.creative_thumb04:
			num = 4;
			showPictureDialog();
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
//			File picture = new File(CommonUtil.getStotagePath(getActivity().getApplicationContext())
//					+ ImageName);
//			startPhotoZoom(Uri.fromFile(picture));
			Uri uri = null;
			if(data.getData() == null){
				Bundle extras = data.getExtras();
				if(extras != null){
					Bitmap image = (Bitmap)extras.getParcelable("data");
					photo =image ;
//					int width = photo.getWidth();
//					int height = photo.getHeight();
					ContentResolver cr = getActivity().getContentResolver();
					String path = MediaStore.Images.Media.insertImage(cr, photo, "myPhoto", "Photo");
					uri = Uri.parse(path); 
					String paths = getFilePathFromUri(uri);
					Bitmap bit= Util.compressImageSize(paths);
					photoProcess(bit);
				}
			}else{
				Bundle extras = data.getExtras();
				if(extras != null){
				photo = (Bitmap)extras.getParcelable("data");
				}
				uri = data.getData();
				String paths = getFilePathFromUri(uri);
				String path = uri.getPath();
				Bitmap bit= Util.compressImageSize(paths);
				int density = bit.getDensity();
				int height = bit.getHeight();
				int width = bit.getWidth();
				photoProcess(bit);
			}
		}

		// 读取相册缩放图片
//		if (requestCode == PHOTOZOOM) {
//			startPhotoZoom(data.getData());
//		}
		// 处理结果
//		if (requestCode == PHOTORESOULT) {
//			Bundle extras = data.getExtras();
//			if (extras != null) {
//				photo = extras.getParcelable("data");
//				photoProcess();
//			}
//
//		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void photoProcess(Bitmap bitmap){
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 -
//		InputStream isBm = new ByteArrayInputStream(stream.toByteArray());
		try {
			if (num == 1) {
				mCreativeThumb01.setImageBitmap(photo);
				String fileName = Util.getStringToday() + ".png";
//				Bitmap bit1 = compress(photo);
				File file = Util.bitmapToFile(getActivity(), bitmap, fileName);
				if (file == null) {
					return;
				}
				files[0] = file;
				mCreativeThumb02.setVisibility(View.VISIBLE);
			} else if (num == 2) {
				
				String fileName = Util.getStringToday() + ".png";
				mCreativeThumb02.setImageBitmap(photo);
				mCreativeThumb03.setVisibility(View.VISIBLE);
//				Bitmap bit2 =compress(photo);
				File file = Util.bitmapToFile(getActivity(), bitmap, fileName);
				if (file == null) {
					return;
				}
				files[1] = file;
				
			} else if (num == 3) {
				String fileName = Util.getStringToday() + ".png";
				mCreativeThumb03.setImageBitmap(photo);
				mCreativeThumb04.setVisibility(View.VISIBLE);
//				Bitmap bit3 = compress(photo);
				File file =Util.bitmapToFile(getActivity(), bitmap, fileName);
				if (file == null) {
					return;
				}
				files[2] = file;
			
			} else if (num == 4) {
				String fileName = Util.getStringToday() + ".png";
				mCreativeThumb04.setImageBitmap(photo);
//				Bitmap bit4 =compress(photo);
				File file = Util.bitmapToFile(getActivity(), bitmap, fileName);
				if (file == null) {
					return;
				}
				files[3] = file;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	// 监听左边菜单
	private void rightButtonListener(View v) {
		popMenu.clearItems();
		if (isClicked == RIGHT) {
			isClicked = RIGHT;
		} else {
			isClicked = RIGHT;
		}
		popMenu.addItems(menuItemsRight);
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.showAsDropDown(v);
		mLayoutRightMenu.setBackgroundResource(R.drawable.bt_right_topbar_selected);
	}
	// 左边下拉菜单的监听
	private void leftButtonListener(View v) {
		popMenu.clearItems();
		if (isClicked == LEFT) {
			isClicked = LEFT;
		} else {
			isClicked = LEFT;
		}
		popMenu.addItems(menuItemsLeft);
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.showAsDropDown(v);
		mLayoutLeftMeun.setBackgroundResource(R.drawable.bt_left_topbar_selected);
	}

	public OnDismissListener lefDdismissListener = new OnDismissListener() {

		@Override
		public void onDismiss() {
			boolean flag1 = isLeftItemClicked;
			boolean flag2 = isReftItemClicked;
			if (isLeftItemClicked == true) {
				isLeftItemClicked = false;
			} else if (isReftItemClicked == true) {
				isReftItemClicked = false;
			} else {
				if (isLeftItemClicked == false) {
					mLayoutLeftMeun.setBackgroundResource(R.drawable.bt_topbar_normal);
					
				}
				if (isReftItemClicked == false) {
					mLayoutRightMenu.setBackgroundResource(R.drawable.bt_topbar_normal);
				}

			}
		}
	};
	// 弹出菜单监听器
	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			System.out.println("下拉菜单点击" + position);

			if (isClicked == LEFT) {
				isLeftItemClicked = true;
				isClicked = 0;
				mLeftButtonSelectedText.setVisibility(View.GONE);
				mLeftButtonSelected.setVisibility(View.VISIBLE);
				mLayoutLeftMeun
						.setBackgroundResource(R.drawable.bt_topbar_normal);
				mLeftButtonSelected.setImageResource(menuItemsLeft[position]);
				isSelected[0] = true;
				if (mHashMap.containsKey("sort_id")) {
					mHashMap.remove("sort_id");
				}

				mHashMap.put("sort_id", String.valueOf(position + 1));
			} else if (isClicked == RIGHT) {
				isReftItemClicked = true;
				isClicked = 0;
				mLayoutRightMenu
						.setBackgroundResource(R.drawable.bt_topbar_normal);
				mRightButtonSelectedText.setVisibility(View.GONE);
				mRightButtonSelected.setVisibility(View.VISIBLE);
				mRightButtonSelected.setImageResource(menuItemsRight[position]);
				isSelected[1] = true;
				if (mHashMap.containsKey("creative_type")) {
					mHashMap.remove("creative_type");
				}
				mHashMap.put("creative_type", String.valueOf(position + 1));

			}
			popMenu.dismiss();
		}
	};

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(getActivity(), "",
				R.drawable.bg_left_idea_item2));
		titlePopup.addAction(new ActionItem(getActivity(), "",
				R.drawable.bg_left_idea_item3));
		titlePopup.addAction(new ActionItem(getActivity(), "",
				R.drawable.bg_left_idea_item4));
		titlePopup.addAction(new ActionItem(getActivity(), "",
				R.drawable.bg_left_idea_item5));
	}

	private void uploadUserImage() {
		mHashMap.put("uid", uid);
		RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestUrl = getString(R.string.modify_thumb);
		vo.mRequestDataMap = mHashMap;
		vo.mFiles = files;
		getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				try {
					if (paramObject == null) {
						return;
					}
					JSONObject object = new JSONObject(paramObject);
					String status = object.getString("status");
					String msg = object.getString("msg");
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		}, Constant.Do_POST_FILES, "正在提交...");
	}

	@Override
	protected void processLogic() {
	}

	@Override
	protected void setListener() {
		mLayoutLeftMeun.setOnClickListener(this);
		mLayoutRightMenu.setOnClickListener(this);
		mLeftButtonSelected.setOnClickListener(this);
		mRightButtonSelected.setOnClickListener(this);
		mSubmitButton.setOnClickListener(this);
		mCreativeThumb01.setOnClickListener(this);
		mCreativeThumb02.setOnClickListener(this);
		mCreativeThumb03.setOnClickListener(this);
		mCreativeThumb04.setOnClickListener(this);
		popMenu.setOnDismissListener(lefDdismissListener);
		mPublicRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton radioButton = (RadioButton) group
								.findViewById(checkedId);
						if (checkedId == R.id.radio_no_public_button) {
							publicId = 0;
						} else {
							publicId = 1;
						}
						radioButton.setChecked(true);
					}
				});
		mOriginalRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.radio_no_original_button) {
							originalId = 0;
						} else {
							originalId = 1;
						}
					}
				});

	}

	private void showPictureDialog() {
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
//							ImageName = "/" + getStringToday() + ".jpg";
							// 调用系统的拍照功能
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							
//							String path = CommonUtil.getStotagePath(getActivity().getApplicationContext());
							
//							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
//									.fromFile(new File(path,
//											ImageName)));
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
	}
	/**
	 * 此方法已经废弃
	 * @author sunbinbin
	 * @param uri
	 *2014-2-27
	 *void
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 800);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}
	/**
	 * 
	 * @author sunbinbin
	 * @param context
	 * @param uri
	 * @return
	 *2014-2-27
	 *String
	 */
	public String getFilePathFromUri(Uri uri){
//		String  myImageUrl = "content://media/external/images/media/***";
	    String[] proj = { MediaStore.Images.Media.DATA };   
	    Cursor actualimagecursor = getActivity().managedQuery(uri,proj,null,null,null);  
	    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
	    actualimagecursor.moveToFirst();   
	    String img_path = actualimagecursor.getString(actual_image_column_index);  
	    File file = new File(img_path);
	    Uri fileUri = Uri.fromFile(file);
	    return img_path;
	}
}
