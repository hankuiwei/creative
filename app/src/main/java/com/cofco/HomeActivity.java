package com.cofco;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.builder.MessageBuilder;
import com.cofco.fragment.FragmentCenter;
import com.cofco.fragment.FragmentHome;
import com.cofco.fragment.FragmentInnovation;
import com.cofco.util.Constant;
import com.cofco.util.LoadImageAsynTask;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.util.PostAsynTask;
import com.cofco.util.PostAsynTask.DoPosteExecute;
import com.cofco.util.Util;
import com.cofco.view.BottomBar;
import com.cofco.view.InOutImageButton;
import com.cofco.view.BottomBar.OnItemChangedListener;
import com.cofco.view.animation.ComposerButtonAnimation;
import com.cofco.view.animation.ComposerButtonGrowAnimationIn;
import com.cofco.view.animation.ComposerButtonGrowAnimationOut;
import com.cofco.view.animation.ComposerButtonShrinkAnimationOut;
import com.cofco.view.animation.InOutAnimation.Direction;
import com.cofco.vo.CreativeItem;
import com.cofco.vo.Message;
import com.cofco.vo.RequestVo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeActivity";
	private static int startNum = 0;
	private static int type = Constant.ALL;
	private ArrayList<CreativeItem> creativeList;
	private HashMap<String, String> mHashMap = new HashMap<String, String>();
	private HashMap mVersionMap = new HashMap<String, String>();
	private View composerButtonsShowHideButton;
	private boolean areButtonsShowing;
	private ViewGroup composerButtonsWrapper;
	private View composerButtonsShowHideButtonIcon;
	private Animation rotateStoryAddButtonIn;
	private Animation rotateStoryAddButtonOut;
	private RelativeLayout mLayoutCancel;
	private ArrayList<Message> messages;
	private LoadImageAsynTask task;
	private PostAsynTask tasks;
	private HashMap requestMap = new HashMap<String, String>();
	private int success;
	private String msg;
	private BottomBar bottomBar;
	private int messageNumber;
	private SharedPreferences sp;
	private Fragment details = (Fragment) getSupportFragmentManager()
			.findFragmentById(R.id.details);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.i(TAG, "1onCreate方法");
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "2onCreate方法");
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
	}

	private void switchDetails(int index, int type) {
		this.type = type;
		Logger.i(TAG, "showDetails");

		Bundle args = new Bundle();
		switch (index) {
		case 0:
			details = new FragmentHome();
			args.putInt("type", this.type);
			details.setArguments(args);
			break;
		case 1:
			details = new FragmentInnovation();
			break;
		case 2:
			details = new FragmentCenter();
			break;
		case 3:
			Intent intent = new Intent();
			intent.setClass(HomeActivity.this, SquareActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		}

		// Execute a transaction, replacing any existing
		// fragment with this one inside the frame.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.details, details);
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
		ft.commit();

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onStop() {
		Logger.i(TAG, "onStop方法");
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		String tag = "onSaveInstanceState";
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		Logger.i(TAG, "onDestroy方法");
		super.onDestroy();
	}

	@Override
	protected void findViewById() {
		composerButtonsShowHideButton = findViewById(R.id.btn_item_five);
		composerButtonsWrapper = (ViewGroup) findViewById(R.id.bottom_main_layout);
		composerButtonsShowHideButtonIcon = findViewById(R.id.bottom_main_bg);
		mLayoutCancel = (RelativeLayout) findViewById(R.id.layout_cancel);
		rotateStoryAddButtonIn = AnimationUtils.loadAnimation(mContext,
				R.anim.rotate_story_add_button_in);
		rotateStoryAddButtonOut = AnimationUtils.loadAnimation(mContext,
				R.anim.rotate_story_add_button_out);
		bottomBar = (BottomBar) findViewById(R.id.ll_bottom_bar);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void processLogic() {
		Log.i(TAG, "processLogic方法");
	}

	@Override
	protected void setListener() {
		mLayoutCancel.setOnTouchListener(ont);
		bottomBar.setOnItemChangedListener(new OnItemChangedListener() {
			@Override
			public void onItemChanged(int index, int type) {
					switchDetails(index, type);
			}
		});
		bottomBar.setSelectedState(0, Constant.ALL);
		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleComposerButtons();
			}
		});

		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			composerButtonsWrapper.getChildAt(i).setOnClickListener(
					new ComposerLauncher(null, new Runnable() {

						@Override
						public void run() {
							new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										Thread.sleep(400);
									} catch (InterruptedException e) {
										e.printStackTrace();
										return;
									}
								}
							}).start();
						}
					}));
		}

		composerButtonsShowHideButton
				.startAnimation(new ComposerButtonGrowAnimationIn(200));
	}

	/**/

	private void toggleComposerButtons() {
		if (!areButtonsShowing) {
			ComposerButtonAnimation.startAnimations(
					this.composerButtonsWrapper, Direction.IN);
			this.composerButtonsShowHideButtonIcon
					.startAnimation(this.rotateStoryAddButtonIn);
			rotateStoryAddButtonIn
					.setInterpolator(new OvershootInterpolator(2F));
			this.composerButtonsShowHideButtonIcon.setVisibility(View.VISIBLE);
			areButtonsShowing = true;
		} else {
			ComposerButtonAnimation.startAnimations(
					this.composerButtonsWrapper, Direction.OUT);
			this.composerButtonsShowHideButtonIcon
					.startAnimation(this.rotateStoryAddButtonOut);
			rotateStoryAddButtonOut.setInterpolator(new AnticipateInterpolator(
					2F));
			this.composerButtonsShowHideButtonIcon.setVisibility(View.GONE);
			areButtonsShowing = false;
		}
	}

	private class ComposerLauncher implements View.OnClickListener {

		@SuppressWarnings("unused")
		public final Runnable DEFAULT_RUN = new Runnable() {

			@Override
			public void run() {
			}
		};
		private final Class<? extends Activity> cls;
		private final Runnable runnable;

		private ComposerLauncher(Class<? extends Activity> c, Runnable runnable) {
			this.cls = c;
			this.runnable = runnable;
		}

		@Override
		public void onClick(View paramView) {
			startComposerButtonClickedAnimations(paramView, runnable);
		}
	}

	@SuppressWarnings("unused")
	private void startComposerButtonClickedAnimations(View view,
			final Runnable runnable) {
		this.areButtonsShowing = false;
		Animation shrinkOut1 = new ComposerButtonShrinkAnimationOut(300);
		Animation shrinkOut2 = new ComposerButtonShrinkAnimationOut(300);
		Animation growOut = new ComposerButtonGrowAnimationOut(300);
		shrinkOut1.setInterpolator(new AnticipateInterpolator(2.0F));
		shrinkOut1.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				composerButtonsShowHideButtonIcon.clearAnimation();
				if (runnable != null) {
					runnable.run();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		ComposerButtonAnimation.Dispear(this.composerButtonsWrapper);
		for (int i = 0; i < this.composerButtonsWrapper.getChildCount(); i++) {
			final View button = this.composerButtonsWrapper.getChildAt(i);
			if (!(button instanceof InOutImageButton))
				continue;
			if (button.getId() == view.getId()) {
				button.startAnimation(growOut);
				this.areButtonsShowing = true;
				toggleComposerButtons();
				switch (view.getId()) {
				case R.id.bottom_main_icon1:
					type = Constant.PRODUCT;
					bottomBar.setNormalState(BottomBar.lastButton);
					bottomBar.setSelectedState(0, type);
					break;
				case R.id.bottom_main_icon2:
					type = Constant.TECHNOLOGY;
					bottomBar.setNormalState(BottomBar.lastButton);
					bottomBar.setSelectedState(0, type);  
					break;
				case R.id.bottom_main_icon3:
					type = Constant.PACK;
					bottomBar.setNormalState(BottomBar.lastButton);
					bottomBar.setSelectedState(0, type);
					break;
				case R.id.bottom_main_icon4:
					type = Constant.MARKTING;
					bottomBar.setNormalState(BottomBar.lastButton);
					bottomBar.setSelectedState(0, type);
					break;
				case R.id.bottom_main_icon5:
					type = Constant.OTHER;
					bottomBar.setSelectedState(0, type);
					bottomBar.setNormalState(BottomBar.lastButton);
					break;
				default:
					break;
				}
			}
		}
		this.composerButtonsShowHideButtonIcon
				.startAnimation(this.rotateStoryAddButtonOut);
	}

	public OnTouchListener ont = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			if (areButtonsShowing == true) {
				toggleComposerButtons();
			}
			return false;
		}
	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		// if (keyCode == event.KEYCODE_BACK )
		// {
		// // 创建退出对话框
		// AlertDialog isExit = new AlertDialog.Builder(this).create();
		// // 设置对话框标题
		// isExit.setTitle("系统提示");
		// // 设置对话框消息
		// isExit.setMessage("确定要退出吗");
		// // 添加选择按钮并注册监听
		// isExit.setButton("确定", listener);
		// isExit.setButton2("取消", listener);
		// // 显示对话框
		// isExit.show();
		//
		// }
		// return false;
		if (keyCode == event.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);

	};

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {

		if (startNum > 10) {
			startNum = 0;
		}
		if (startNum % 10 == 3 || startNum == 0) {
			getVersionNumber();
		}
		startNum++;
		super.onResume();
	}

	@Override
	protected void onStart() {
		getMessageInfo();
		boolean flag = sp.getBoolean("isNew", false);
		int num = sp.getInt("msgNum", 0);
		int newNum = sp.getInt("newNum", 0);
		if (flag) {
			if(newNum>0){
				bottomBar.setMessageNum(newNum);
			}else{
				bottomBar.setMessageNum(num);
			}
		}else{
			bottomBar.clearMessageNum();
		}
		super.onStart();
	}

	/**
	 * 获取版本号，并更新
	 */
	public void getVersionNumber() {
		if(!NetUtil.hasNetwork(mContext)){
			return;
		}
		String number = Util.getVersionNumber(this);
		requestMap.put("andriod_version_number", number);
		RequestVo vo = new RequestVo();
		vo.mContext = this;
		vo.mRequestUrl = getString(R.string.version_num);
		vo.mRequestDataMap = requestMap;
		PostAsynTask task = new PostAsynTask(new DoPosteExecute() {
			@Override
			public void doAfterPost(String result) {
				if (result != null) {
					// Toast.makeText(HomeActivity.this, "实行结果  ", 0).show();
					try {
						JSONObject object = new JSONObject(result);
						String status = object.getString("status");
						if (status.equals("2") || status.equals("200")) {
							JSONObject data = object.getJSONObject("data");
							String versionNum = data
									.getString("andriod_number");
							String verNum = Util
									.getVersionNumber(HomeActivity.this);
							float sub = Float.valueOf(versionNum)
									- Float.valueOf(verNum);
							Editor editor = sp.edit();
							if (sub > 0) {
								// Toast.makeText(HomeActivity.this, "实行结 "+sub,
								// 0).show();
								
								editor.putFloat("version", sub);
								editor.commit();
//								bottomBar.showUpdateInfo();
							}else{
								editor.putFloat("version", 0);
								editor.commit();
							}
							mVersionMap.put("andriod_number", versionNum);
							String appUrl = data.getString("andriod_url");
							mVersionMap.put("appUrl", appUrl);
							UpdateManager manager = new UpdateManager(
									HomeActivity.this);
							// // 检查软件更新
							manager.checkUpdate(mVersionMap);
						} else if(status.endsWith("0")){
							Editor editor = sp.edit();
							editor.putFloat("version", 0);
							editor.commit();
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return;
					}

				} else {
					return;
				}
			}
		});
		task.execute(vo);

	}

	/**
	 * 获取消息数目
	 */
	private void getMessageInfo() {
		if(!NetUtil.hasNetwork(mContext)){
			return;
		}
		HashMap mHashMap = new HashMap();
		// sp = getApplicationContext().getSharedPreferences("account",
		// Context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		mHashMap.put("uid", uid);
		mContext = getApplicationContext();
		final RequestVo vo = new RequestVo();
		vo.mContext = getApplicationContext();
		vo.mRequestDataMap = mHashMap;
		vo.mRequestUrl = getString(R.string.message_list);
		vo.mJsonBuilder = new MessageBuilder();
		tasks = new PostAsynTask(new DoPosteExecute() {
			@Override
			public void doAfterPost(String result) {
				if(result==null){
					return;
				}
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getString("status");
					if (status.equals("200")) {
						// 数据获取成功
						messages = (ArrayList<Message>) vo.mJsonBuilder
								.parseJSON(result);

						if (messages != null) {
							int num = messages.size();
							/*
							 * sp = getSharedPreferences("messageNum",
							 * Context.MODE_PRIVATE);
							 */
							int resultNum = sp.getInt("msgNum", 0);
							Editor editor = sp.edit();
							if (resultNum >=0) {
								if (num > resultNum) {
									bottomBar.setMessageNum(num - resultNum);
									editor.putInt("msgNum", num);
									if(!sp.getBoolean("isNew", false)){
										editor.putInt("newNum", num-resultNum);
									}
									editor.putBoolean("isNew", true);
									editor.commit();
								}else if(num<=resultNum){
									editor.putInt("msgNum", num);
									editor.putBoolean("isNew", false);
									editor.commit();
								}
									
							} /*
							 * else if (num > 0) { bottomBar.setMessageNum(num);
							 * editor.putInt("msgNum", num); editor.commit(); }
							 */

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		});
		tasks.execute(vo);
	}
}