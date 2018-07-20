package com.cofco.adapter;

import java.util.List;

import com.cofco.vo.UserInfo;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ����Ҫд��ע�ͣ�����д������
 * @author bin
 *
 */
public class UserInfoAdapter extends BaseAdapter {

	private Context mContext;
	private List<UserInfo> mItemList;
	private LayoutInflater mLayoutInflater;
	public UserInfoAdapter(Context context,List<UserInfo> arrayList){
		this.mContext = context;
		this.mItemList = arrayList;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		View view = convertView;
		ViewHolder holder;
		if(view==null){
			view = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
			holder = new ViewHolder();
			holder.mImageView = (ImageView) view.findViewById(R.id.icon);
			holder.mTextView = (TextView) view.findViewById(R.id.edit);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		//��ȡItem�����
		UserInfo info = mItemList.get(position);
		//��holder����Ŀؼ����и�ֵ
		holder.mTextView.setText(info.getUsername());
		return view;
	}
	static class ViewHolder{
		ImageView mImageView;
		TextView mTextView;
	}
}
