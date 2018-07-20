package com.cofco.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cofco.R;
import com.cofco.view.BannerLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

/**
 */
public class Util {
	/**
	 */
	public static int getScreenWidth(Context context) {
		if (context == null) {
			return 720;
		}
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 */
	public static int getScreenHeight(Context context) {
		if (context == null) {
			return 720;
		}
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static float getScreenDeisityDip(Context context) {
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	/**
	 */
	public static int dip2px(Context context, float px) {
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}

	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		return df.format(new Date()).toString();
	}

	public static String getVersionNumber(Context context) {
		String versionCode = null;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					"com.cofco", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return versionCode;
	}

	public static void showToast(Context context, String message, int position) {
		Toast mToast = Toast.makeText(context, message, 0);
		mToast.setGravity(Gravity.CENTER, 0, -position);
		mToast.setDuration(0);
		mToast.show();
	}

	public static void showToast(Context context, String message) {
		Toast mToast = Toast.makeText(context, message, 0);
		mToast.setGravity(Gravity.CENTER, 0, 50);
		mToast.setDuration(0);
		mToast.show();
	}

	public static String getDate(String unixDate) {
		long unixLong = 0;
		String date = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			unixLong = Long.parseLong(unixDate) * 1000;
		} catch (Exception ex) {
			return "";
		}
		try {
			date = simpleDateFormat.format(unixLong);
			return date.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public static void showImageView(final Context context,
			final BannerLayout bannerLayout, ImageLoader imageLoader,
			ArrayList<String> images) {
		if (context == null) {
			return;
		}
		ImageSize imageSize = new ImageSize(getScreenWidth(context), 384);
		//显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				//.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.loading_bg)
				.cacheInMemory(true)
				//.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		ViewGroup.LayoutParams lp = bannerLayout.getLayoutParams();
				for (int i = 0; i < images.size(); i++) {
					ImageView imageView = new ImageView(context);
					imageView.setScaleType(ImageView.ScaleType.FIT_XY);

					//imageView.setLayoutParams(new god.LayoutParams(god.LayoutParams.MATCH_PARENT,displayMetrics.widthPixels/2));
					imageView.setLayoutParams(lp);
					imageLoader.displayImage(images.get(i), imageView, options);
					bannerLayout.addView(imageView);
				}



		/*for (int i = 0; i < images.size(); i++) {
			imageLoader.displayImage(images.get(i),bannerLayout,ImageLoader.getInstance());
			imageLoader.loadImage(images.get(i), imageSize,
					new SimpleImageLoadingListener() {
						// 对下载的图片进行缩放，以适应不同分辨率的屏幕
						// 缩放的比例scale=density(每英寸的像素dpi/240)//图片默认的像素比
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							ImageView imageView = new ImageView(context);
							BitmapDrawable drw = new BitmapDrawable(loadedImage);
							imageView.setAdjustViewBounds(true);
							Matrix matrix = new Matrix();
							int bitmapWidth = loadedImage.getWidth();
							int bitmapHeight = loadedImage.getHeight();
							float deviceDensity = Util
									.getScreenDeisityDip(context);
							float scale = deviceDensity / 240;
							matrix.postScale(scale, scale);
							Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0,
									0, bitmapWidth, bitmapHeight, matrix, true);


							// 设置参数
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
							//BitmapFactory.decodeFile(imagePath, options);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							//loadedImage.compress(Bitmap.CompressFormat.JPEG, options, baos);
							int height = options.outHeight;
							int width= options.outWidth;
							int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
							int minLen = Math.min(height, width); // 原图的最小边长
							if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
								float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
								inSampleSize = (int)ratio;
							}
							options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
							options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
							Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件





							bitmap = compressImageByQuality(imageUri);
							imageView.setImageBitmap(bitmap);
							bannerLayout.addView(imageView);
							super.onLoadingComplete(imageUri, view, loadedImage);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							Logger.i("下载图片", "失败");
							super.onLoadingFailed(imageUri, view, failReason);
						}
					});
		}
*/
	}

	/**
	 * 格式化imgUrl
	 * 
	 * @author wuchx
	 * @date 2014-1-21 下午1:34:31
	 * @param imgUrl
	 * @return
	 */
	public static String formatImgUrl(String imgUrl) {
		String url = "";
		if (imgUrl != null && !"".equals(imgUrl)) {
			int position = imgUrl.indexOf("/");
			if (position > 0) {
				url = imgUrl.substring(position);
			} else {
				url = imgUrl;
			}
		} else {
			url = "";
		}
		return url;
	}

	/**
	 * 将bitmap转为file
	 * 
	 * @param context
	 * @param bitmap
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static File bitmapToFile(Context context, Bitmap bitmap, String filename)
			throws Exception {
		File f = new File(context.getCacheDir(), filename);
		f.createNewFile();
		// Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();
		// write the bytes in file
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(bitmapdata);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}
	// 获取系统当前时间将照片命名为系统时间
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	// /**
	// * 对图片的大小进行缩放
	// * @author sunbinbin
	// * @param srcPath
	// * @return
	// *2014-2-27
	// *Bitmap
	// */
	public static Bitmap compressImageSize(String srcPath) {
		float ww; //输出的宽度
		float hh; //输出的高度
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bitmap为空
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		newOpts.inJustDecodeBounds = false;
		if (w > h) {
			// 如果宽度大于高度则表明照片是翻转90度的
			ww = 1280f;// 这里设置高度为1280f
			hh = 720f;// 这里设置宽度为720f
		} else {
			// 现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
			hh = 1280f;// 这里设置高度为1280f
			ww = 720f;// 这里设置宽度为720f
		}
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		/**
		 * 这里遇到一种情况就是三星的手机拍照的时候图片会翻转90'
		 */
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		} else if (w > h && w < ww) {
			be = (int) (ww / newOpts.outWidth);
		} else if (w < h && h < hh) {
			be = (int) (hh / newOpts.outHeight);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return Util.compressImageByQuality(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 对图像的质量进行压缩
	 * 
	 * @author sunbinbin
	 * @param image
	 * @return 2014-2-27 Bitmap
	 */
	private static Bitmap compressImageByQuality(Bitmap image) {
		return null;
	}
	//private static Bitmap compressImageByQuality(Bitmap image) {
	private static Bitmap compressImageByQuality(String  imagePath) {
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

*/
		// 设置参数
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
		BitmapFactory.decodeFile(imagePath, options);

		int height = options.outHeight;
		int width= options.outWidth;
		int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
		int minLen = Math.min(height, width); // 原图的最小边长
		if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
			float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
			inSampleSize = (int)ratio;
		}
		options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
		options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
		Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
		Log.w("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
		/*imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setImageBitmap(bm);*/
		return bm;
	}

	public static  Bitmap compressImageSize(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		newOpts.inJustDecodeBounds = false;
		// 现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
		float hh = 1280f;// 这里设置高度为1280f
		float ww = 720f;// 这里设置宽度为720f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		} else if (w > h && w < ww) {
			be = (int) (ww / newOpts.outWidth);
		} else if (w < h && h < hh) {
			be = (int) (hh / newOpts.outHeight);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImageByQuality(bitmap);// 压缩好比例大小后再进行质量压缩
	}

}
