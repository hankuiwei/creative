package com.cofco.fragment;

import com.cofco.R;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.ThreadPoolManager;
import com.cofco.util.Util;
import com.cofco.vo.RequestVo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment{
	
	protected ProgressDialog mProgressDialog;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private static Context mContext;
	private ThreadPoolManager mThreadPoolManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		initImageLoader(getActivity());
		super.onCreate(savedInstanceState);
	}
	public BaseFragment() {
		
		mThreadPoolManager = ThreadPoolManager.getInstance();
	}

	/**
	 * 
	 */

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
//					Util.showToast(mContext, "网络错误!");
 					return;
				} else {
					callBack.processData(msg.obj, true);
					
				}
			} else if (msg.what == Constant.NET_FAILED) {
//				CommonUtil.showInfoDialog(mContext,
//						getString(R.string.net_error));
				Util.showToast(mContext, "请检查您的网络设置!");
				return;
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
					//从请求类型判断使用哪个方法进行请求
					if (type == Constant.DO_GET) {
						obj = NetUtil.doGet(reqVo);
					} else if (type == Constant.DO_POST) {
						obj = NetUtil.doPost(reqVo);
					} else if (type == Constant.DO_POST_FILE) {
						obj = NetUtil.doPostWithFile(reqVo);
					}else if(type==Constant.Do_POST_FILES){
						obj = NetUtil.doPostWithFiles(reqVo);
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
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack,int requestType,String msg) {
		showProgressDialog(msg);
		BaseHandler handler = new BaseHandler(getActivity(), callBack, reqVo);
		BaseTask taskThread = new BaseTask(mContext, reqVo, handler, requestType);
		this.mThreadPoolManager.addTask(taskThread);
	}

	/**
	 * 显示加载项
	 */
	protected void showProgressDialog(String msg) {
		if ( (this.mProgressDialog == null)) {
			this.mProgressDialog = new ProgressDialog(mContext,R.style.MyDialogStyle);
		}
		this.mProgressDialog.setMessage(msg);
		this.mProgressDialog.show();
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
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

	/**
	 */
	protected void closeProgressDialog() {
		if (this.mProgressDialog != null)
			this.mProgressDialog.dismiss();
	}

	/**
	 */
	protected abstract void findViewById(View view);
	
	/**
	 */
	protected abstract void processLogic();

	/**
	 */
	protected abstract void setListener();
}
