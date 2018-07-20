package com.cofco.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.cofco.R;
import com.cofco.vo.RequestVo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @author bin
 * 
 */
public class NetUtil {
	private static final String TAG = "NetUtil";
	private static BasicHeader[] headers = new BasicHeader[10];
	static {
		headers[0] = new BasicHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");

	}

	/**
	 * 
	 * @param vo
	 * @return json
	 */
	public static String doPost(RequestVo vo) {
		if (vo.mContext == null) {
			return null;
		} 
		if(!NetUtil.hasNetwork(vo.mContext)) {
			CommonUtil.showInfoDialog(vo.mContext, "请检查您的网络连接!");
			return null;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		//String url = vo.mContext.getString(R.string.app_host).concat("&do=" + vo.mRequestUrl);
		String url = vo.mContext.getString(R.string.app_host).concat(vo.mRequestUrl);
		Logger.i(TAG, url);

		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);// 连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);// 数据传输时间

		HttpPost post = new HttpPost(url);
		// post.setHeaders(headers);
		Object obj = null;
		try {
			if (vo.mRequestDataMap != null) {
				HashMap<String, String> map = vo.mRequestDataMap;
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				// pairList.add(new BasicNameValuePair("do", vo))
				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(
							entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
				post.setEntity(entity);
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Logger.i(TAG, result);
				if (result == null) {
					return null;
				} else {
					return result;
				}

			}
		} catch (ClientProtocolException e) {
			Logger.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		} catch (Exception e) {
			Logger.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param vo
	 *            请求参数
	 * @return json对象
	 */
	public static String doGet(RequestVo vo) {
		if (!NetUtil.hasNetwork(vo.mContext)) {
			CommonUtil.showInfoDialog(vo.mContext, "请检查您的网络连接!");
			return null;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		//String url = vo.mContext.getString(R.string.app_host).concat("&do=" + vo.mRequestUrl);
		String url = vo.mContext.getString(R.string.app_host).concat(vo.mRequestUrl);
		Log.i(TAG, url);
		HttpGet get = new HttpGet(url);
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);// 连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);// 数据传输时间
		// get.setHeaders(headers);
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				if (result == null || result.equals("")) {
					Log.i(NetUtil.class.getSimpleName(), result);
				}
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static String doGetData(RequestVo vo) {
		Object obj = null;
		DefaultHttpClient client = new DefaultHttpClient();
		String path = vo.mContext.getString(R.string.app_host).concat(
				"&do=" + vo.mRequestUrl);
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(50000);
			conn.setRequestProperty("Charset", "utf-8");
			conn.setDoInput(true);
			InputStream inputStream = conn.getInputStream();
			Log.i(TAG, path);
			ByteArrayOutputStream bos = null;
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = conn.getInputStream();
				bos = new ByteArrayOutputStream();
				if (inputStream != null) {
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = inputStream.read(b)) > 0) {
						bos.write(b, 0, len);
					}
				}
				byte[] byteArray = bos.toByteArray();
				String config = new String(byteArray);

				bos.flush();
				bos.close();
				inputStream.close();
				conn.disconnect();
				return config;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
		//
	}

	/**
	 * ]
	 * 
	 * @param vo
	 *            请求参数
	 * @param file
	 *            上传的文件
	 * @return json对象
	 * @throws Exception
	 */
	public static Object doPostWithFile(RequestVo vo) throws Exception {
		if (!NetUtil.hasNetwork(vo.mContext)) {
			CommonUtil.showInfoDialog(vo.mContext, "请检查您的网络连接!");
			return null;
		}
		MultipartEntity entity = new MultipartEntity();
		DefaultHttpClient client = new DefaultHttpClient();
		//String url = vo.mContext.getString(R.string.app_host).concat("&do=" + vo.mRequestUrl);
		String url = vo.mContext.getString(R.string.app_host).concat(vo.mRequestUrl);
		HttpPost post = new HttpPost(url);
		// post.setHeaders(headers);
		Object obj = null;

		try {
			if (vo.mRequestDataMap != null) {
				HashMap<String, String> map = vo.mRequestDataMap;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String str = entry.getValue();
					entity.addPart(key, new StringBody(str));
				}
				if (vo.mFile != null && vo.mFile.exists()) {

					entity.addPart("avatar", new FileBody(vo.mFile));
				}
			}
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				String sds = result;
				return result;
			}
		} catch (ClientProtocolException e) {
			Logger.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			Logger.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		}
		return null;
	}

	public static Object doPostWithFiles(RequestVo vo) throws Exception {
		if (!NetUtil.hasNetwork(vo.mContext)) {
			CommonUtil.showInfoDialog(vo.mContext, "请检查您的网络连接!");
			return null;
		}
		MultipartEntity entity = new MultipartEntity();
		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.mContext.getString(R.string.app_host).concat(
				"&do=" + vo.mRequestUrl);
		HttpPost post = new HttpPost(url);
		// post.setHeaders(headers);
		Object obj = null;

		try {
			if (vo.mRequestDataMap != null) {
				HashMap<String, String> map = vo.mRequestDataMap;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String str = entry.getValue();
					entity.addPart(key,
							new StringBody(str, Charset.forName("UTF-8")));
				}
				if (vo.mFiles != null && vo.mFiles.length > 0) {
					int len = vo.mFiles.length;
					for (int i = 0; i < vo.mFiles.length; i++) {
						if (vo.mFiles[i] != null) {
							entity.addPart("avatar" + String.valueOf(i),
									new FileBody(vo.mFiles[i]));
						}
					}

				}
			}
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				String sds = result;
				return result;
			}
		} catch (ClientProtocolException e) {
			Logger.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			Logger.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
			return null;
		}
		return null;
	}

	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return 如果网络不通，返回false默认返回true
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		NetworkInfo wifiInfo = con
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if ((workinfo == null || !workinfo.isAvailable())
				& !wifiInfo.isConnected()) {
			// Toast mToast = Toast.makeText(context, R.string.net_error,
			// Toast.LENGTH_SHORT);
			// mToast.setGravity(Gravity.CENTER, 0, -60);
			// mToast.show();
			return false;
		}
		return true;
	}
}
