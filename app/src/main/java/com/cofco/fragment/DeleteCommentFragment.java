package com.cofco.fragment;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.cofco.CenterFavoriteActivity;
import com.cofco.R;
import com.cofco.ReplayActivity;
import com.cofco.util.CommonUtil;
import com.cofco.util.Constant;
import com.cofco.util.LoadImageAsynTask;
import com.cofco.util.NetUtil;
import com.cofco.util.PostAsynTask;
import com.cofco.util.LoadImageAsynTask.DoGetExecute;
import com.cofco.util.PostAsynTask.DoPosteExecute;
import com.cofco.vo.Comment;
import com.cofco.vo.RequestVo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteCommentFragment extends DialogFragment{
	private int type;
	private int deleteId;
	private boolean isDeleted=false;
	static onChangedListener listeners;
	private Comment comment;
	private HashMap mHashMap = new HashMap();
	public static DeleteCommentFragment newInstance(String title,Comment comment,int type,onChangedListener listener) {
		DeleteCommentFragment myDialogFragment = new DeleteCommentFragment();
			Bundle bundle = new Bundle();
		  bundle.putString("title", title);
		  bundle.putInt("type", type);
		  bundle.putSerializable("comment", comment);
		  myDialogFragment.setArguments(bundle);
		  listeners = listener;
		  return myDialogFragment;
		 }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		type = getArguments().getInt("type");
		String message;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setItems(new String[]{"删除","取消"}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					comment = (Comment) getArguments().get("comment");
					String commentId = comment.getComment_id();
					String creativeId = comment.getCreative_id();
					deleteItem(commentId, creativeId, which);
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
	
	private void deleteItem(String commentId,String creative_Id,int type){
		if(!NetUtil.hasNetwork(getActivity())){
			CommonUtil.showInfoTost(getActivity(), getString(R.string.net_errors));
			return;
		}
		RequestVo vo = new RequestVo();
		vo.mContext = getActivity();
		vo.mRequestDataMap = mHashMap;
			mHashMap.put("creative_id", creative_Id);
			mHashMap.put("comment_id", commentId);
			vo.mRequestUrl =getString(R.string.comment_delete);
		PostAsynTask task = new PostAsynTask(new DoPosteExecute() {
			@Override
			public void doAfterPost(String result) {
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
						Constant.UPDATE_LIST = true;
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
