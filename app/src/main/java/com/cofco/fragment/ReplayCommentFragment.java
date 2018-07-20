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

public class ReplayCommentFragment extends DialogFragment{
	private int type;
	private Comment comment ;
	public static ReplayCommentFragment newInstance(String title,Comment comment,int type) {
		ReplayCommentFragment myDialogFragment = new ReplayCommentFragment();
			Bundle bundle = new Bundle();
		  bundle.putString("title", title);
		  bundle.putInt("type", type);
		  bundle.putSerializable("comment", comment);
		  myDialogFragment.setArguments(bundle);
		  return myDialogFragment;
		 }
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		type = getArguments().getInt("type");
		 comment = (Comment) getArguments().get("comment");
		String message;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(type ==Constant.MESSAGE){
			builder.setTitle(title);
		}else if(type ==Constant.FAVORITE){
			builder.setTitle(title);
		}else{
			builder.setTitle(title);
		}
	
		builder.setItems(new String[]{"回复","取消"}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intent =getActivity().getIntent();
					intent.setClass(getActivity(), ReplayActivity.class);
					intent.putExtra("comment", comment);
					getActivity().startActivityForResult(intent, 0);
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
	
	public interface onChangedListener{
		public void onDataChanged(boolean isDeleted);
		
	}
}
