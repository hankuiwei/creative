package com.cofco.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cofco.R;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.view.animation.ComposerButtonAnimation;
import com.cofco.view.animation.ComposerButtonGrowAnimationIn;
import com.cofco.view.animation.ComposerButtonGrowAnimationOut;
import com.cofco.view.animation.ComposerButtonShrinkAnimationOut;
import com.cofco.view.animation.InOutAnimation.Direction;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 
 * @author Great Han   
 * @time 2018/7/16 11:32
 * @email 315807323@qq.com 
 */
public class BottomBar extends LinearLayout implements OnClickListener {
	private Context mContext;

	private static final int TAG_0 = 0;
	private static final int TAG_1 = 1;
	private static final int TAG_2 = 2;
	private static final int TAG_3 = 3;
	private static final int TAG_4 = 4;
	private int type;
	private View composerButtonsShowHideButton;
	private boolean areButtonsShowing;
	private ViewGroup composerButtonsWrapper;
	private View composerButtonsShowHideButtonIcon;
	private Animation rotateStoryAddButtonIn;
	private Animation rotateStoryAddButtonOut;
	RelativeLayout rl_aninationmenu_cancle;
	private FrameLayout mLayoutMessageNum;
	// private Button mMessageNum;
	private ImageView mUpdateInfo;
	private TextView mMessageNum;
	private TextView tvOne;

	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public BottomBar(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private List<View> itemList;

	/**
	 * get the buttons from layout
	 * 
	 * 
	 * */
	private void init() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.bottom_bar, null);
		layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
		// tvOne = (TextView)layout.findViewById(R.id.tv_warming);
		ImageView btnOne = (ImageView) layout.findViewById(R.id.btn_item_one);
		ImageView btnTwo = (ImageView) layout.findViewById(R.id.btn_item_two);
		ImageView btnThree = (ImageView) layout
				.findViewById(R.id.btn_item_three);
		ImageView btnFour = (ImageView) layout.findViewById(R.id.btn_item_four);
		ImageView btnFive = (ImageView) layout.findViewById(R.id.btn_item_five);
		mLayoutMessageNum = (FrameLayout) layout
				.findViewById(R.id.frame_layout_num);
		mMessageNum = (TextView) layout
				.findViewById(R.id.fragment_message_notification_text);
		mUpdateInfo = (ImageView) findViewById(R.id.fragment_innovation_new);
		btnOne.setOnClickListener(this);
		btnTwo.setOnClickListener(this);
		btnThree.setOnClickListener(this);
		btnFour.setOnClickListener(this);
		btnFive.setOnClickListener(this);
		btnOne.setTag(TAG_0);
		btnTwo.setTag(TAG_1);
		btnThree.setTag(TAG_2);
		btnFour.setTag(TAG_3);
		btnFive.setTag(TAG_4);
		itemList = new ArrayList<View>();
		itemList.add(btnOne);
		itemList.add(btnTwo);
		itemList.add(btnThree);
		itemList.add(btnFour);
		itemList.add(btnFive);
		this.addView(layout);
	}

	public void setMessageNum(int num) {
		if(mLayoutMessageNum==null){
			mLayoutMessageNum = (FrameLayout) findViewById(R.id.frame_layout_num);
		}
		this.mLayoutMessageNum.setVisibility(View.VISIBLE);
		if(mMessageNum==null){
			mMessageNum = (TextView) findViewById(R.id.fragment_message_notification_text);
		}
		this.mMessageNum.setText(String.valueOf(num));
	}

	public void clearMessageNum() {
		mLayoutMessageNum.setVisibility(View.INVISIBLE);
		// this.mMessageNum.setVisibility(View.INVISIBLE);
	}

	public void showUpdateInfo() {
		if (mUpdateInfo == null) {
			mUpdateInfo = (ImageView) findViewById(R.id.fragment_innovation_new);
		}
		mUpdateInfo.setVisibility(View.VISIBLE);
	}

	public void hideUpdateInfo() {
		if (mUpdateInfo == null) {
			mUpdateInfo = (ImageView) findViewById(R.id.fragment_innovation_new);
		}
		mUpdateInfo.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		switch (tag) {
		case TAG_0:
			setNormalState(lastButton);
			type = Constant.ALL;
			setSelectedState(tag, type);
			break;
		case TAG_1:
			setNormalState(lastButton);
			setSelectedState(tag, type);
			break;
		case TAG_2:
			setNormalState(lastButton);
			setSelectedState(tag, type);
			break;
		case TAG_3:
			setNormalState(lastButton);
			setSelectedState(tag, type);
			break;
		case TAG_4:

			break;
		}
	}

	public static int lastButton = -1;

	/**
	 * set the default bar item of selected
	 * */
	public void setSelectedState(int index, int type) {
		if (index != -1 && onItemChangedListener != null) {
			if (index > itemList.size()) {
				throw new RuntimeException(
						"the value of default bar item can not bigger than string array's length");
			}
			itemList.get(index).setSelected(true);
			onItemChangedListener.onItemChanged(index, type);
			lastButton = index;
		}
	}

	protected void setListener() {

		// rl_aninationmenu_cancle.setOnTouchListener(ont);
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
									}
								}
							}).start();
						}
					}));
		}

		composerButtonsShowHideButton
				.startAnimation(new ComposerButtonGrowAnimationIn(200));

	}

	/**
	 * set the normal state of the button by given index
	 * 
	 * 
	 * */
	public void setNormalState(int index) {
		if (index != -1) {
			if (index > itemList.size()) {
				throw new RuntimeException(
						"the value of default bar item can not bigger than string array's length");
			}
			itemList.get(index).setSelected(false);
		}
	}

	/**
	 * make the red indicate VISIBLE
	 * 
	 * ������ִ�а�ť���Ͻǵĺ�ɫСͼ��Ŀɼ�
	 * 
	 * */
	public void showIndicate(int value) {
		tvOne.setText(value + "");
		tvOne.setVisibility(View.VISIBLE);
	}

	/**
	 * make the red indicate GONE
	 * 
	 * 
	 * */
	public void hideIndicate() {
		tvOne.setVisibility(View.GONE);
	}

	public interface OnItemChangedListener {
		public void onItemChanged(int index, int type);
	}

	private OnItemChangedListener onItemChangedListener;

	public void setOnItemChangedListener(
			OnItemChangedListener onItemChangedListener) {
		this.onItemChangedListener = onItemChangedListener;
	}

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
				this.areButtonsShowing = false;
				switch (view.getId()) {
				case R.id.bottom_main_icon1:
					break;
				case R.id.bottom_main_icon2:
					break;
				case R.id.bottom_main_icon3:
					break;
				case R.id.bottom_main_icon4:
					break;
				case R.id.bottom_main_icon5:
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

}
