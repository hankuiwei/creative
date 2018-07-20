package com.cofco;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.adapter.MessageAdapter;
import com.cofco.builder.MessageBuilder;
import com.cofco.fragment.DeleteDialogFragment;
import com.cofco.fragment.DeleteDialogFragment.onChangedListener;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.NetUtil;
import com.cofco.util.Util;
import com.cofco.vo.Message;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 我的消息
 * @date 2013-12-03
 * @author sunbin
 *
 */
public class MessageActivity extends BaseActivity{
   private ImageView mBackButton;
   private ListView mListView;
   private MessageAdapter messageAdapter;
   private SharedPreferences sp;
   private String uid;
   private HashMap mHashMap = new HashMap<String, String>();
   private ArrayList<Message> messageList;
	@Override
	protected void findViewById() {
		mBackButton  = (ImageView) this.findViewById(R.id.back);
		mListView = (ListView) this.findViewById(R.id.center_message_list);
	}

	public void setMessageList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.center_my_message);
		sp = getSharedPreferences("account", Context.MODE_PRIVATE);
		uid =sp.getString("uid", "");
		if(uid.equals("")){
			return;
		}
		Editor editor = sp.edit();
		editor.putBoolean("isNew", false);
		editor.commit();
	}
	@Override
	protected void processLogic() {
		getMessageList();
	}

	@Override
	protected void setListener() {
		mBackButton.setOnClickListener(this);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					final int position, long id) {
				Message msg = messageList.get(position);
				DeleteDialogFragment  dialog = DeleteDialogFragment.newInstance("我的消息", msg.getId(), Constant.MESSAGE,new onChangedListener() {
					@Override
					public void onDataChanged(boolean isDeleted) {
							if(isDeleted){
							messageList.remove(position);
							messageAdapter.setMessageList(messageList);
							mListView.setAdapter(messageAdapter);
							}else{
								Util.showToast(MessageActivity.this, "删除失败!");
								return;
							}
					}
				});
				dialog.show(getSupportFragmentManager(), "favorite");
				return true;
			}
			
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;

		default:
			break;
		}
	}

	private void getMessageList() {
		if(!NetUtil.hasNetwork(MessageActivity.this)){
			CommonUtil.showInfoTost(MessageActivity.this, getString(R.string.net_errors));
			return;
		}
		mHashMap.put("uid", uid);
		final RequestVo vo = new RequestVo();
		vo.mContext = MessageActivity.this;
		vo.mRequestUrl = getString(R.string.message_list);
		vo.mRequestDataMap = mHashMap;
		vo.mJsonBuilder = new MessageBuilder();
		getDataFromServer(vo, new DataCallback<String>() {

			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				if(paramObject==null||paramObject.equals("")){
					Toast mToast =Toast.makeText(MessageActivity.this, "消息获取失败!", 0);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.setDuration(0);
					mToast.show();
				}else{
					try {
						JSONObject object = new JSONObject(paramObject);
						String status = object.getString("status");
						if(status.equals("200")){
							//数据获取成功
							messageList =(ArrayList<Message>) vo.mJsonBuilder.parseJSON(paramObject);
							if(messageList==null){
								Toast mToast =Toast.makeText(MessageActivity.this, "消息获取失败!", 0);
								mToast.setGravity(Gravity.CENTER, 0, 0);
								mToast.setDuration(0);
								mToast.show();
							}else{
								if(messageAdapter==null){
									messageAdapter = new MessageAdapter(MessageActivity.this, messageList);
									mListView.setAdapter(messageAdapter);
								}else{
									mListView.setAdapter(messageAdapter);
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return;
					}
					
				}
			}
		}, Constant.DO_POST, "正在获取消息!");
	}

}
