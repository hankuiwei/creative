package com.cofco.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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

public class PopMenuText {
	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private PopAdapter adapter;
	private int width;

	// private OnItemClickListener listener;
	public PopMenuText(Context context, int width) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.width = width;
		itemList = new ArrayList<String>();
		View view = LayoutInflater.from(context).inflate(R.layout.popmenu_text, null);

		listView = (ListView) view.findViewById(R.id.pop_list_view);
		if (adapter == null) {
			adapter = new PopAdapter();
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);

		popupWindow = new PopupWindow(view, (width / 3) * 2, LayoutParams.WRAP_CONTENT);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		// this.listener = listener;
		listView.setOnItemClickListener(listener);
	}

	public void setOnTouchOutside(OnTouchListener outListener) {
		popupWindow.setTouchInterceptor(outListener);
	}

	public void setOnDismissListener(OnDismissListener dismissListener) {
		popupWindow.setOnDismissListener(dismissListener);
	}

	public void addItems(List<String> items) {
		if (adapter == null) {
			adapter = new PopAdapter();
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		for (String s : items)
			itemList.add(s);
	}

	public void clearItems() {
		itemList.clear();
	}

	public void addItem(String item) {
		itemList.add(item);
	}

	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent, width / 6, context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));
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
				convertView = LayoutInflater.from(context).inflate(R.layout.pomenu_text_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.groupItem = (TextView) convertView.findViewById(R.id.tv_menu_item);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.groupItem.setText(itemList.get(position));
			return convertView;
		}

		private final class ViewHolder {
			TextView groupItem;
			ImageView imageView;
		}
	}
}
