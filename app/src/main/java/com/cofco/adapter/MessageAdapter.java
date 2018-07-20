package com.cofco.adapter;

import java.util.ArrayList;

import com.cofco.R;
import com.cofco.util.Util;
import com.cofco.vo.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class MessageAdapter extends BaseAdapter{
private Context mContext;
private ArrayList<Message> messageList;
private LayoutInflater mLayoutInflater;


	public MessageAdapter(Context context, ArrayList<Message> messageList) {
	this.mContext = mContext;
	this.messageList = messageList;
	this.mLayoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}
	
	public void setMessageList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	@Override
	public int getCount() {
		if(messageList!=null){
			return messageList.size();
		}
		return 0;
	}
	
	@Override
	public Object getItem(int position) {
		
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		View view = convertView;
		ViewHolder holder = null;
		if(view==null){
			view = mLayoutInflater.inflate(R.layout.center_my_message_item, null);
			holder = new ViewHolder();
			holder.mMessageContent = (TextView) view.findViewById(R.id.center_message_content);
			holder.mMessageTime = (TextView) view.findViewById(R.id.center_message_time);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
			Message message = (Message) getItem(position);
			holder.mMessageContent.setText(message.getContent());
			holder.mMessageTime.setText(Util.getDate(message.getMktime()));
		return view;
	}
	static class ViewHolder {
		TextView mMessageContent;
		TextView mMessageTime;
	}
}
