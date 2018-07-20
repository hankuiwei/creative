package com.cofco.fragment;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.CenterFavoriteActivity;
import com.cofco.R;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.LoadImageAsynTask;
import com.cofco.util.Logger;
import com.cofco.util.NetUtil;
import com.cofco.util.PostAsynTask;
import com.cofco.util.LoadImageAsynTask.DoGetExecute;
import com.cofco.util.PostAsynTask.DoPosteExecute;
import com.cofco.vo.RequestVo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment{
	private static final String TAG  = "DeleteDialogFragment";
	private int type;
	private int deleteId;
	private boolean isDeleted=false;
	static onChangedListener listeners;
	private HashMap mHashMap = new HashMap();
	public static DeleteDialogFragment newInstance(String title,int deleteId,int type,onChangedListener listener) {
		DeleteDialogFragment myDialogFragment = new DeleteDialogFragment();
			Bundle bundle = new Bundle();
		  bundle.putString("title", title);
		  bundle.putInt("type", type);
		  bundle.putInt("deleteId", deleteId);
		  myDialogFragment.setArguments(bundle);
		  listeners = listener;
		  return myDialogFragment;
		 }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		type = getArguments().getInt("type");
		String message;
		deleteId = getArguments().getInt("deleteId");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(type ==Constant.MESSAGE){
			builder.setTitle(title);
		}else if(type ==Constant.FAVORITE){
			builder.setTitle(title);
		}
	
		builder.setItems(new String[]{"删除","取消"}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					deleteItem(deleteId, type);
					break;
				case 1:
					break;
				default:
					break;
				}
			}
		});
		return builder.create();
	}
	
	private void deleteItem(int id,int type){
		if(!NetUtil.hasNetwork(getActivity())){
			CommonUtil.showInfoTost(getActivity(), getString(R.string.net_errors));
			return;
		}
		RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestDataMap = mHashMap;
		if(type==Constant.FAVORITE){
			mHashMap.put("collect_id", String.valueOf(id));
			vo.mRequestUrl = getString(R.string.delete_favorite);
		}else if(type == Constant.MESSAGE){
			mHashMap.put("message_id", String.valueOf(id));
			vo.mRequestUrl =getString(R.string.delete_message);
		}else{
			return;
		}
		PostAsynTask task = new PostAsynTask(new DoPosteExecute() {
			
			@Override
			public void doAfterPost(String result) {
				Logger.e(TAG, result);
				String status = null;
				if(result==null){
					isDeleted = false;
					return;
				}
				JSONObject object;
				try {
					object = new JSONObject(result);
					if(object.has("status")){
						status =object.getString("status");
					}
					if(status.equals("200")){
						listeners.onDataChanged(true);
						isDeleted =true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}
		});
		task.execute(vo);
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public interface onChangedListener{
		public void onDataChanged(boolean isDeleted);
		
	}
}
