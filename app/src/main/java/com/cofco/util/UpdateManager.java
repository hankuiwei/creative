package com.cofco.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.cofco.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 
 * @author sunbin
 *
 */

public class UpdateManager
{
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMapURL = new HashMap<String, String>();
	/* 下载保存路径 */
	private String mSavePath;
	private String serviceCode;
	/* 记录进度条数量 */
	private int progress;
	public ProgressDialog pBar;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				int progresss = progress;
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	private void doNewVersionUpdate() {
		StringBuffer sb = new StringBuffer();
		Dialog dialog = new AlertDialog.Builder(mContext)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(mContext);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							}

						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(HashMap mHashMap)
	{	
		String serviceCode;
		String currentCode = Util.getVersionNumber(mContext);
		if(NetUtil.hasNetwork(mContext)){
			
			if (null != mHashMap){
 				serviceCode = (String) mHashMap.get("andriod_number");
				if (Float.valueOf(serviceCode) > Float.valueOf(currentCode)){
					showNoticeDialog();
					mHashMapURL = mHashMap;
				}
			}else
			{
//				Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
				return ;
			}
		}
	}
	public void checkUpdateCenter(HashMap mHashMap)
	{	
		String serviceCode;
		String currentCode = Util.getVersionNumber(mContext);
		if(NetUtil.hasNetwork(mContext)){
			
			if (null != mHashMap){
				serviceCode = (String) mHashMap.get("andriod_number");
				if (Float.valueOf(serviceCode) > Float.valueOf(currentCode)){
					showNoticeDialog();
					mHashMapURL = mHashMap;
				}else
				{
					Toast mToast =Toast.makeText(mContext, "已是最新版本!", 0);
					mToast.setGravity(Gravity.CENTER, 0, -40);
					mToast.setDuration(0);
					mToast.show();
					return ;
				}
			}
		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */

	protected String getString(int appHost) {
		return null;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private String getVersionCode(Context context)
	{	
		String versionCode = null ;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.cofco", 0).versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
//		ImageView imageview = new ImageView(mContext);
//		imageview.setBackgroundResource(R.drawable.toast_frame);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
 	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setTitle(R.string.soft_updating);
				// 给下载对话框增加进度条
				final LayoutInflater inflater = LayoutInflater.from(mContext);
				View v = inflater.inflate(R.layout.softupdate_progress, null);
				mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
				builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
//		mDownloadDialog = builder.create();
//		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					String urlw = mHashMapURL.get("appUrl");
					Logger.e("AppPath", urlw);
					URL url = new URL(mHashMapURL.get("appUrl"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "creative.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						int progrs = progress;
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			if(mDownloadDialog!=null){
				mDownloadDialog.dismiss();
			}
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, "creative.apk");
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
