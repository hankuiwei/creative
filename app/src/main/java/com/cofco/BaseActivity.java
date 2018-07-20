package com.cofco;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.util.ThreadPoolManager;
import com.cofco.util.Util;
import com.cofco.vo.RequestVo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public abstract class BaseActivity extends FragmentActivity implements
		View.OnClickListener {
	private static final String TAG = "BaseActivity";
	protected ProgressDialog mProgressDialog;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected Context mContext;
	protected int mScreenHeight;
	private ThreadPoolManager mThreadPoolManager;

	public BaseActivity() {
		mThreadPoolManager = ThreadPoolManager.getInstance();
	}

	/**
	 */
	@Override
	public void onCreate(Bundle paramBundle) {
		mContext = getApplicationContext();
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initImageLoader(getApplicationContext());
		initView();
	}

	/**
	 * 
	 */
	private void initView() {
		loadViewLayout();
		findViewById();
		setListener();
		processLogic();
	}

	class BaseHandler extends Handler {
		private Context mContext;
		private DataCallback callBack;
		private RequestVo reqVo;

		public BaseHandler(Context context, DataCallback callBack,
				RequestVo reqVo) {
			this.mContext = context;
			this.callBack = callBack;
			this.reqVo = reqVo;
		}

		public void handleMessage(Message msg) {
			closeProgressDialog();
			if (msg.what == Constant.SUCCESS) {
				if (msg.obj == null) {
					// CommonUtil.showInfoTost(mContext, "");
					return;
				} else {
					callBack.processData(msg.obj, true);
				}
			} else if (msg.what == Constant.NET_FAILED) {
				// CommonUtil.showInfoDialog(mContext,
				// getString(R.string.net_error));
				return;
				// CommonUtil.showInfoTost(mContext, "");
			}
		}
	}

	class BaseTask implements Runnable {
		private Context mContext;
		private RequestVo reqVo;
		private Handler handler;
		private int type;

		public BaseTask(Context context, RequestVo reqVo, Handler handler,
				int requestType) {
			this.mContext = context;
			this.reqVo = reqVo;
			this.handler = handler;
			this.type = requestType;
		}

		@Override
		public void run() {
			Object obj = null;
			Message msg = new Message();
			if (NetUtil.hasNetwork(mContext)) {
				try {
					// 从请求类型判断使用哪个方法进行请求
					if (type == Constant.DO_GET) {
						obj = NetUtil.doGet(reqVo);
					} else if (type == Constant.DO_POST) {
						obj = NetUtil.doPost(reqVo);
					} else if (type == Constant.DO_POST_FILE) {
						obj = NetUtil.doPostWithFile(reqVo);
					}
					if (obj == null) {
						int i = 2;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				msg.what = Constant.SUCCESS;
				msg.obj = obj;
				handler.sendMessage(msg);
			} else {
				msg.what = Constant.NET_FAILED;
				msg.obj = obj;
				handler.sendMessage(msg);
			}
		}

	}

	public interface DataCallback<T> {
		public void processData(T paramObject, boolean paramBoolean);
	}

	/**
	 * @param reqVo
	 * @param callBack
	 */
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack,
			int requestType, String msg) {
		showProgressDialog(msg);
		BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTask taskThread = new BaseTask(this, reqVo, handler, requestType);
		this.mThreadPoolManager.addTask(taskThread);
	}

	/**
	 */
	protected void showProgressDialog(String msg) {
		if ((!isFinishing()) && (this.mProgressDialog == null)) {
			this.mProgressDialog = new ProgressDialog(this,
					R.style.MyDialogStyle);
		}
		this.mProgressDialog.setMessage(msg);
		this.mProgressDialog.show();
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	protected void onStop() {
		Logger.i(TAG, " onStop");
		super.onStop();
	}

	/**
	 */
	protected void closeProgressDialog() {
		if (this.mProgressDialog != null)
			this.mProgressDialog.dismiss();
	}

	/**
	 */
	protected abstract void findViewById();

	/**
	 */
	protected abstract void loadViewLayout();

	/**
	 */
	protected abstract void processLogic();

	/**
	 */
	protected abstract void setListener();

}
