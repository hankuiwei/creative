package com.cofco.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.cofco.R;

public class PopMenu {
	private ArrayList<Integer> itemList;
	private ArrayList<Integer> itemsListRight;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private PopAdapter adapter;
	// private OnItemClickListener listener;
	public PopMenu(Context context,int width) {
		// TODO Auto-generated constructor stub
		this.context = context;
		itemList = new ArrayList<Integer>(5);
		itemsListRight = new ArrayList<Integer>(3);
		View view = LayoutInflater.from(context)
				.inflate(R.layout.popmenu, null);

		listView = (ListView) view.findViewById(R.id.pop_list_view);
		if(adapter==null){
			adapter = new PopAdapter();
			listView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);
		int listwidth = view.getWidth();
//		popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
//		popupWindow = new PopupWindow(view, context.getResources().getDimensionPixelSize(R.dimen.popmenu_width),LayoutParams.WRAP_CONTENT);
		
		popupWindow = new PopupWindow(view, width/2,LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		// this.listener = listener;
		listView.setOnItemClickListener(listener);
	}
	public void setOnTouchOutside(OnTouchListener outListener){
		popupWindow.setTouchInterceptor(outListener);
	}
	
	public void setOnDismissListener(OnDismissListener dismissListener){
		popupWindow.setOnDismissListener(dismissListener);
	}
	public void addItems(int[] items) {
		if(adapter==null){
			adapter = new PopAdapter();
			listView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		for (int s : items)
			itemList.add(s);
	}
	public void addItemsRight(int[] items){
		for(int s:items){
			itemList.add(s);
		}
	}
	public void clearItems(){
		itemList.clear();
	}
	public void addItem(int item) {
		itemList.add(item);
	}

	public void showAsDropDown(View parent) {
		int width = parent.getWidth();
		popupWindow.showAsDropDown(parent,0,
				context.getResources().getDimensionPixelSize(
						R.dimen.popmenu_yoff));
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}
	public void dismiss() {
		popupWindow.dismiss();
	}

	private final class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.pomenu_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.imageView = (ImageView) convertView.findViewById(R.id.menu_item);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView.setImageResource(itemList.get(position));
			return convertView;
		}

		private final class ViewHolder {
			TextView groupItem;
			ImageView imageView;
		}
	}
}
