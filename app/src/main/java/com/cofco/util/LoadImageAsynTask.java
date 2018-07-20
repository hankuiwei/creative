package com.cofco.util;

import com.alibaba.fastjson.serializer.DoubleArraySerializer;
import com.cofco.vo.RequestVo;

import android.os.AsyncTask;
/**
 * 执行异步任务
 * @author sunbin
 *
 */
public class LoadImageAsynTask extends AsyncTask<RequestVo, Void, String> {

	public DoGetExecute doGetExecute;
	public interface DoGetExecute{
		public void doAfterPost(String result);
	}
	
	public LoadImageAsynTask(DoGetExecute doPostExecute) {
		this.doGetExecute = doPostExecute;
	}




	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	
	

	@Override
	protected String doInBackground(RequestVo... params) {
		String result =NetUtil.doGet(params[0]);
		if(result==null){
			return null;
		}
		return result;
	}




	@Override
	protected void onPostExecute(String result) {
		if(result==null){
			return;
		}
		if(doGetExecute==null){
			int i = 3;
			int j = 4;
		}
		doGetExecute.doAfterPost(result);
		super.onPostExecute(result);
	}


}
