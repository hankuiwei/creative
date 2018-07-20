package com.cofco.util;

import com.cofco.vo.RequestVo;

import android.os.AsyncTask;
/**
 * 执行异步任务
 * @author sunbin
 *
 */
public class PostAsynTask extends AsyncTask<RequestVo, Void, String> {
	public DoPosteExecute doPostExecute;
	public interface DoPosteExecute{
		public void doAfterPost(String result);
	}
	
	public PostAsynTask(DoPosteExecute doPostExecute) {
		this.doPostExecute = doPostExecute;
	}




	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	
	

	@Override
	protected String doInBackground(RequestVo... params) {
 		String result =NetUtil.doPost(params[0]);
		if(result==null){
			return null;
		}
		return result;
	}




	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		doPostExecute.doAfterPost(result);
	}


}
