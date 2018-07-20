package com.cofco;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.cofco.adapter.ShareAdapter;
import com.cofco.util.Constant;
import com.cofco.util.Logger;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 分享app
 * 
 * @author sunbin
 * 
 */

public class ShareActivity extends Activity {
	private static final String LOG_TAG = "ShareActivity";
	private ListView mListView;
	private ShareAdapter adapter;
	private String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadViewLayout();
		findViewById();
		processLogic();
		setListener();
	}

	private void findViewById() {
		mListView = (ListView) findViewById(R.id.share_list);
	}

	private void loadViewLayout() {
		setContentView(R.layout.share_bg);
		setTitle("分享给好友");
		message = getString(R.string.share_message) + Constant.APP_SHARE_URL;
		ShareSDK.initSDK(this);
	}

	private void processLogic() {
		adapter = new ShareAdapter(this);
		mListView.setAdapter(adapter);
	}

	private void setListener() {
		mListView.setOnItemClickListener(onItemClick);
	}

	OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long Id) {
			switch (position) {
			case 0:
				Platform.ShareParams sp = new SinaWeibo.ShareParams();
				sp.text = message;
				// sp.imagePath ="";
				Platform weibo = ShareSDK.getPlatform(ShareActivity.this,
						SinaWeibo.NAME);
				weibo.setPlatformActionListener(sinalistener); // 设置分享事件回调
				// 执行图文分享
				weibo.share(sp);
				break;
			case 1:
				Platform.ShareParams ssp = new TencentWeibo.ShareParams();
				ssp.text = message;
				// ssp.imagePath = "";
				Platform tewb = ShareSDK.getPlatform(ShareActivity.this,
						TencentWeibo.NAME);
				tewb.setPlatformActionListener(sinalistener);
				tewb.share(ssp);
				tewb.removeAccount();
				break;
			case 2:
				// 朋友圈分享
				 WechatMoments.ShareParams ps = new
				 WechatMoments.ShareParams();
				 ps.shareType = Platform.SHARE_TEXT;
				 ps.title = "分享到朋友圈";
				 ps.text = message;
				 ps.imagePath = "";
				 // sp.imagePath = “/mnt/sdcard/测试分享的图片.jpg”;
				 Platform wechats = ShareSDK.getPlatform(ShareActivity.this,
				 WechatMoments.NAME);
				 System.out.println(wechats + "wwwwwwww");
				 wechats.setPlatformActionListener(sinalistener); // 设置分享事件回调
				 // wechat.removeAccount(); //用于清除登陆账户
				 // 执行图文分享
				 wechats.share(ps);
				 finish();
//				Intent intentjoin = new Intent();
//				intentjoin.setType("image/*");
//				intentjoin.putExtra(Intent.EXTRA_SUBJECT, "Share");
//				intentjoin.putExtra(Intent.EXTRA_TEXT, message);
//				intentjoin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intentjoin.putExtra(Intent.EXTRA_STREAM, Uri.parse(""));   
//				startActivity(Intent.createChooser(intentjoin, ""));
				break;
			case 3:
				sendMail();
				break;
			case 4:
				sendSms();
				break;
			}
		}
	};

	/**
	 * 邮件分享
	 * 
	 * @param emailBody
	 */
	private void sendMail() {
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		String emailSubject = "分享app";
		// 设置邮件默认地址
		// email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
		// 设置邮件默认标题
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
		// 设置要默认发送的内容
		email.putExtra(android.content.Intent.EXTRA_TEXT, message);
		// 调用系统的邮件系统
		startActivityForResult(Intent.createChooser(email, "请选择邮件发送软件"), 1001);
		finish();
	}

	/**
	 * 短信分享
	 */
	private void sendSms() {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
		intent.putExtra("sms_body", message);
		startActivity(intent);
		finish();
	}

	PlatformActionListener sinalistener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			System.out.println("授权失败..........");
			Message msg = new Message();
			msg.obj = arg2;
			sharehand.sendMessage(msg);
			System.out.println(arg2);
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
			System.out.println("onComplete........");

			/*
			 * 使用腾讯微博登陆后获取的用户id和昵称 String tid = arg2.get("openid").toString();
			 * String nickName = arg2.get("name").toString();
			 */
			/*
			 * 使用新浪微博登陆后获取的用户id和昵称 String sid = arg2.get("id").toString();
			 * String nickName = arg2.get("name").toString();
			 */
			Message msg = new Message();
			msg.obj = "yes";
			sharehand.sendMessage(msg);
			Toast.makeText(ShareActivity.this, "分享成功!", 0).show();
			finish();
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			finish();
		}
	};
	Handler sharehand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String isShare = msg.obj.toString();
			if ("yes".equals(isShare)) {
				Toast.makeText(ShareActivity.this, "分享成功", 0).show();
				finish();
			} else
				Toast.makeText(ShareActivity.this, "授权失败", 0).show();
			finish();
		};
	};
}
